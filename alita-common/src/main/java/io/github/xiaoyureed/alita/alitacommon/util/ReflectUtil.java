package io.github.xiaoyureed.alita.alitacommon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReflectUtil {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);


    private ReflectUtil() {
    }
    
    public static Object createInstance(String className) {
        return newInstance(className);
    }
    
    public static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object newInstance(String fullClassName) {
        if (StringUtil.isValid(fullClassName)) {
            try {
                Class<?> clazz = Class.forName(fullClassName);
                return clazz.newInstance();
            } catch (ClassNotFoundException e) {
                log.error(">>> Class not found, full class name: [{}]", fullClassName);
            } catch (IllegalAccessException e) {
                log.error("something goes wrong while new instance according to given full class name", e);
            } catch (InstantiationException e) {
                //e.printStackTrace();
                log.error("something goes wrong while new instance according to given full class name", e);
            }
        }
        return null;
    }
}
