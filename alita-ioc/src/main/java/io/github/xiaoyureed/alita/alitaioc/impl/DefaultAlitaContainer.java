package io.github.xiaoyureed.alita.alitaioc.impl;

import io.github.xiaoyureed.alita.alitacommon.util.ReflectUtil;
import io.github.xiaoyureed.alita.alitacommon.util.StringUtil;
import io.github.xiaoyureed.alita.alitaioc.AlitaContainer;
import io.github.xiaoyureed.alita.alitaioc.annotation.Bean;
import io.github.xiaoyureed.alita.alitaioc.annotation.Inject;
import io.github.xiaoyureed.alita.alitaioc.exception.InvalidBasePackageException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author xiaoyu
 * date: 2020/7/20
 */
public class DefaultAlitaContainer implements AlitaContainer {
    private static final Logger log = LoggerFactory.getLogger(DefaultAlitaContainer.class);

    /**
     * key - bean simple name
     * value - bean instance
     */
    private Map<String, Object> beanMap = new ConcurrentHashMap<String, Object>();

    /**
     * key: 注入关系, 如 beanSimpleName.fieldName
     * value: bean's name(beansMap的key)
     */
    private Map<String, String> relationMap = new ConcurrentHashMap<String, String>();

    private DefaultAlitaContainer() {
    }

    private static class DefaultAlitaContainerHolder {
        private static final DefaultAlitaContainer me = new DefaultAlitaContainer();
    }

    public static DefaultAlitaContainer me() {
        return DefaultAlitaContainerHolder.me;
    }

    public Object getByName(String name) {
        Object instance = this.beanMap.get(name);
        return instance;
    }

    public <T> T getByType(Class<T> clazz) {
        Bean beanAnno = clazz.getAnnotation(Bean.class);
        if (beanAnno != null
                && StringUtil.isValid(beanAnno.value())) {// use specified beanName
            String specifiedName = beanAnno.value();
            log.debug("class [{}]指定了有效beanName[{}]", clazz.getName(), specifiedName);
            return (T) this.beanMap.get(specifiedName);
        } else {// use beanSimpleName
            String simpleName = clazz.getSimpleName();
            log.debug("class [{}]没指定有效的beanName, 使用默认值(simple bean name)", clazz.getName());
            return (T) this.beanMap.get(simpleName);
        }
    }

    public void register(String basePackage) {
        Set<Class<?>> clazzSet;
        try {
            clazzSet = scanPackage(basePackage);
        } catch (ClassNotFoundException e) {
            log.error("Error of scanning package");
            throw new RuntimeException(e);
        }

        init(clazzSet);

        inject();
    }

    /**
     * 注入
     */
    private void inject() {
        Iterator<Map.Entry<String, String>> ite = relationMap.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, String> next  = ite.next();
            String                    value = next.getValue();
            String                    key   = next.getKey();
            String[]                  split = key.split("\\.");
            try {
                PropertyUtils.setProperty(this.beanMap.get(split[0]), split[1], this.beanMap.get(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 创建bean实例, 创建注入关系
     * (也就是填充两个map)
     *
     */
    private void init(Set<Class<?>> clazzSet) {

        for (Class<?> clazz : clazzSet) {
            Object bean = null;
            try {
                bean = ReflectUtil.createInstance(clazz);
            } catch (Exception e) {
                log.error("创建bean[{}]出现问题", clazz.getName(), e);
            }

            String specName = clazz.getAnnotation(Bean.class).value();
            if (StringUtil.isValid(specName)) {
                this.beanMap.put(specName, bean);
            }
            // 没有指定beanname, 使用bean 的 qualifiedName 注册
            else {
                this.beanMap.put(clazz.getName(), bean);
            }

            // 初始化 relationMap
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                Inject injectAnno = f.getAnnotation(Inject.class);
                if (injectAnno != null) {
                    // 指定了注入的name, 那么使用指定的name
                    if (StringUtil.isValid(injectAnno.value())) {
                       // this.relationMap.put(clazz.getName().concat(".").concat(f.getName()), injectAnno.value());
                        this.relationMap.put(specName.concat(".").concat(f.getName()), injectAnno.value());
                    }
                    // 没有指定injection name, 则使用当前 field name
                    else {
                       // this.relationMap.put(clazz.getName() + "." + f.getName(), f.getName());
                        this.relationMap.put(specName + "." + f.getName(), f.getName());
                    }
                }
            }

        }


    }

    /**
     * 扫描所有class
     *
     * @param packageName
     * @return
     */
    private Set<Class<?>> scanPackage(String packageName) throws ClassNotFoundException {
        Set<Class<?>> result = new LinkedHashSet<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // classpath 下所有的资源文件
        Enumeration<URL> urls = null;
        try {
            urls = classLoader.getResources(packageName.replace(".", "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (urls != null && urls.hasMoreElements()) {
            URL    url      = urls.nextElement();
            String protocol = url.getProtocol();

            // 文本中的 class
            if ("file".equals(protocol)) {
                try {
                    File packageFile = new File(URLDecoder.decode(url.getFile(), "UTF-8"));

                    if (!packageFile.exists()
                            || !packageFile.isDirectory()) {
                        log.error("package: [{}]无效", url.getFile());
                        throw new InvalidBasePackageException();
                    }

                    File[] files = packageFile.listFiles();

                    handleFiles(files, result, classLoader, packageName);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    log.error(">>> Error of handing files");
                    throw e;
                }
            }
            // jar 中的 class
            else if ("jar".equals(protocol)) {
                JarURLConnection jarConn;
                try {
                    jarConn = (JarURLConnection) url.openConnection();
                    JarFile               jarFile  = jarConn.getJarFile();
                    Enumeration<JarEntry> jarEntry = jarFile.entries();
                    // TODO
//                    handleJarFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                log.error("获取package下资源的protocal出错.");
            }
        }


        return result;
    }

    private void handleFiles(File[] files, Set<Class<?>> result, ClassLoader classLoader, String packageName) throws ClassNotFoundException {
        for (File f : files) {
            // it's a folder, 递归
            if (f.isDirectory()) {
                handleFiles(f.listFiles(), result, classLoader, packageName);
            }
            // 是文件
            else {
                try {
                    Class<?> clazz = classLoader.loadClass(packageName + "." + f.getName().substring(0, f.getName().lastIndexOf(".")));
                    // 被 @Bean 注解才扫描
                    Bean beanAnno = clazz.getAnnotation(Bean.class);
                    if (beanAnno != null) {
                        /*// specified name
                        if (StringUtil.isValid(beanAnno.value()) {

                        }
                        // 默认, 使用 bean 的 SimpleName
                        else {

                        }*/
                        result.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.error("扫描文件[{}]发生错误", f.getPath(), e);
                    throw e;
                }
            }
        }

    }
}
