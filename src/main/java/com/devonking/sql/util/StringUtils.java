package com.devonking.sql.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class StringUtils {

    private static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    private static final String DELIMITER_COMMA_SPACE = ", ";
    private static final String DELIMITER_SINGLE_SPACE = " ";

    /**
     * 删除尾部字符
     *
     * @param sb
     * @param tail
     */
    public static void trimTail(StringBuilder sb, String tail) {
        if(null != sb && sb.toString().endsWith(tail)) {
            sb.delete(sb.length() - tail.length(), sb.length());
        }
    }

    public static String format(Map<String, Object> params) {
        StringBuilder str = new StringBuilder("Parameters: ");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FULL);
        params.forEach((k, v) -> {
            str.append("(").append(k).append(")").append(DELIMITER_SINGLE_SPACE);
            if (v instanceof List) {
                List items = (List) v;
                items.forEach(itm -> str.append(v).append(DELIMITER_COMMA_SPACE));
            } else if (v instanceof Date) {
                str.append(sdf.format(v)).append(DELIMITER_COMMA_SPACE);
            } else {
                str.append(v).append(DELIMITER_COMMA_SPACE);
            }
        });

        StringUtils.trimTail(str, DELIMITER_COMMA_SPACE);

        return str.toString();
    }

    public static String defaultString(String str, String defStr) {
        return Objects.isNull(str)? defStr: str;
    }

    public static String joinWith(String separator, boolean skipNull, Object... objects) {
        if (objects == null) {
            throw new IllegalArgumentException("Object varargs must not be null");
        } else {
            String sanitizedSeparator = defaultString(separator, "");
            StringBuilder result = new StringBuilder();
            Iterator iterator = Arrays.asList(objects).iterator();

            while(iterator.hasNext()) {
                Object obj = iterator.next();
                if (skipNull && Objects.isNull(obj)) {
                    continue;
                }

                String value = Objects.toString(obj, "");
                result.append(value);
                if (iterator.hasNext()) {
                    result.append(sanitizedSeparator);
                }
            }

            return result.toString();
        }
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
}
