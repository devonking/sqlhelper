package com.devonking.sql.util;

import java.util.Collection;
import java.util.Map;

/**
 * 对象操作工具类
 *
 * @author Devon King
 * @since 1.0
 */
public class ObjectUtil {

    /**
     * 比较两个对象是否不相等
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean notEqual(Object o1, Object o2) {
        return !equal(o1, o2);
    }

    /**
     * 比较两个对象是否相等
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean equal(Object o1, Object o2) {
        return (null != o1 && o1.equals(o2)) || (null == o1 && null == o2);
    }

    /**
     * 比较两个字符串对象是否相等
     *
     * @param o1
     * @param o2
     * @param ignoreCase
     * @return
     */
    public static boolean equal(String o1, String o2, boolean ignoreCase) {
        if(ignoreCase) {
            return (null != o1 && o1.equalsIgnoreCase(o2)) || (null == o1 && null == o2);
        } else {
            return equal(o1, o2);
        }
    }

    /**
     * 比较是否大于零的正数
     *
     * @param n
     * @return
     */
    public static boolean positive(Long n) {
        return null != n && n > 0;
    }

    /**
     * 比较是否等于零
     *
     * @param n
     * @return
     */
    public static boolean zero(Long n) {
        return equal(n, 0);
    }

    /**
     * 比较是否小于零的负数
     *
     * @param n
     * @return
     */
    public static boolean negative(Long n) {
        return null != n && n < 0;
    }

    /**
     * 判断对象是否为NULL
     *
     * @param o
     * @return
     */
    public static boolean isNull(Object o) {
        return null == o;
    }

    /**
     * 判断对象是否不为NULL
     *
     * @param o
     * @return
     */
    public static boolean notNull(Object o) {
        return !isNull(o);
    }

    /**
     * 判断是否为空集合
     *
     * @param c
     * @return
     */
    public static boolean empty(Collection c) {
        return null == c || c.isEmpty();
    }

    /**
     * 判断是否为空集合
     *
     * @param m
     * @return
     */
    public static boolean empty(Map m) {
        return null == m || m.size() == 0;
    }

    /**
     * 判断是否为非空集合
     *
     * @param c
     * @return
     */
    public static boolean notEmpty(Collection c) {
        return !empty(c);
    }

    /**
     * 判断是否为非空集合
     *
     * @param m
     * @return
     */
    public static boolean notEmpty(Map m) {
        return !empty(m);
    }

    /**
     * 判断字符串对象是否为空
     * @param s
     * @return
     */
    public static boolean blank(String s) {
        return null == s || s.trim().length() == 0;
    }

    /**
     * 判断字符串对象是否不为空
     *
     * @param s
     * @return
     */
    public static boolean notBlank(String s) {
        return !blank(s);
    }

    public static String getString(Object obj, String defStr) {
        return null == obj? defStr: obj.toString();
    }

    public static <T> T get(T obj, T def) {
        return null == obj? def: obj;
    }

    public static boolean isTrue(Boolean bool) {
        return null != bool && bool == true;
    }

    public static boolean isFalse(Boolean bool) {
        return null != bool && bool == false;
    }

    public static boolean nonFalse(Boolean bool) {
        return !isFalse(bool);
    }

    public static boolean nonTrue(Boolean bool) {
        return !isTrue(bool);
    }
}
