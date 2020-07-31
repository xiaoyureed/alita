package io.github.xiaoyureed.alita.alitacommon.util;

public final class StringUtil {

    private StringUtil() {
    }
    
    public static boolean isValid(String str) {
        return (str != null) && (!"".equals(str.trim()));
    }

    public static boolean isNumeric(String str) {
        if (isValid(str) && str.matches("\\d*")) {
            return true;
        }
        return false;
    }
}
