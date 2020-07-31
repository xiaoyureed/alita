package io.github.xiaoyureed.alita.alitacommon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/19 13:06
 * @Description: message utils
 */
public final class MessageUtils {

    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    private MessageUtils(){}

    public static String format(String template, String... params) {
        if (isTemplate(template)) {

            String[] split = template.split("\\{}");

            if (checkPlaceholderAndParams(split, params)) {

                StringBuilder result = new StringBuilder(split[0]);
                for (int i=0; i<params.length; i++) {
                    result.append(params[i]);
                    result.append(split[i+1]);
                }

                return result.toString();
            } else {
                log.error(">>> placeholder & params are not matching");
                return null;
            }

        }
        log.error(">>> message template was null");
        return null;
    }

    /**
     * 检测 pattern 和 params个数是否符合
     * @param arr
     * @param params
     * @return
     */
    private static boolean checkPlaceholderAndParams(String[] arr, String... params) {
        if (params.length == arr.length-1) {
            return true;
        }
        return false;
    }

    private static boolean isTemplate(String template) {
        if (StringUtil.isValid(template) && template.contains("{}")) {
            return true;
        }
        return false;
    }
}
