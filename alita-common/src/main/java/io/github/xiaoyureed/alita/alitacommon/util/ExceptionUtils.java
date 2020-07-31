package io.github.xiaoyureed.alita.alitacommon.util;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/19 15:32
 * @Description: exception utils
 */
public final class ExceptionUtils {

    private ExceptionUtils(){}

    public static void runtimeException(String msg) {
        throw new RuntimeException(msg);
    }

    public static void runtimeException(String template, String... params) {
        throw new RuntimeException(MessageUtils.format(template, params));
    }

    public static void runtimeException(String msg, Throwable throwable) {
        throw new RuntimeException(msg, throwable);
    }

    public static void runtimeException(String template, Throwable throwable, String... params) {
        runtimeException(MessageUtils.format(template, params), throwable);
    }
}
