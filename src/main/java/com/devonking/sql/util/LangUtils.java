package com.devonking.sql.util;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class LangUtils {
    /**
     * 变量开始标签
     */
    private static final String VARIABLE_START_TAG = "${";
    /**
     * 变量结束标签
     */
    private static final String VARIABLE_END_TAG = "}";

    /**
     * 是否变量
     *
     * @param name
     * @return
     */
    public static boolean isVar(String name) {
        if(name.startsWith(VARIABLE_START_TAG) && name.endsWith(VARIABLE_END_TAG)) {
            return true;
        }
        return false;
    }

    /**
     * 获取变量名
     *
     * @param name
     * @return
     */
    public static String var(String name) {
        if(isVar(name)) {
            String nm = name.substring(2);
            return nm.substring(0, nm.length() - 1);
        }
        return name;
    }
}
