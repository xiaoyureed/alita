package io.github.xiaoyureed.alita.alitaioc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author xiaoyu
 * date: 2020/7/20
 */
public class ClassResourceDemo {
    private static final Logger log = LoggerFactory.getLogger(ClassResourceDemo.class);

    @Test
    public void demo1() {
        ClassLoader loader1 = ClassResourceDemo.class.getClassLoader();
        ClassLoader loader2 = Thread.currentThread().getContextClassLoader();
        System.out.println(loader1 == loader2);// true
    }

    @Test
    public void demo2() throws Exception {
//        log.error("{}", "异常", new Exception());
        ClassLoader classLoader = this.getClass().getClassLoader();
        // 只会搜寻当前class所在的文件夹和parent文件夹
        URL url = classLoader.getResource("com.xiaoyu.ioc".replace(".", "/"));
        String path = url.getPath();
        log.info("getResource--path: [{}]", path);
        URI uri = url.toURI();
        log.info("uri: [{}]", uri);
        log.info("-----------------------");

        Enumeration<URL> resources = classLoader.getResources("demo.test".replaceAll("\\.", "/"));
        iterateEnumeration(resources);
        log.info("------------------------------");
        // 会搜寻整个classpath下的符合条件的资源
        Enumeration<URL> urls = classLoader.getResources("com.xiaoyu.ioc".replace(".", "/"));
        iterateEnumeration(urls);
        log.info("---------------------");

        URL fileUrl = classLoader.getResource("demo");
        String filePath = fileUrl.getFile();
        log.info("fileUrl: [{}]", filePath);
        log.info("------------------------------");
        File fileDir = new File(filePath);
        File[] listFiles = fileDir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".class")) {
                    return true;
                }
                return false;
            }
        });

        for (File f: listFiles) {
            log.info("fileName: [{}]", f.getName());
            log.info("test---: [{}], [{}]", f.getAbsolutePath(), f.getPath());
        }
    }

    private void iterateEnumeration(Enumeration<URL> urls) {
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String path = url.getPath();

            // 若是包, getFile的结果和getPaht相同, 都是包路径; 用来处理文件安路径(xxx/yy/Demo)会报错
            String file = url.getFile();

            log.info("getResources --path: [{}]", path);
            log.info("print fileName: [{}]", file);
        }
    }
}
