package com.devonking.sql.filter;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public enum Operator {

    /**
     * 等于 x = y
     */
    EQ(0),
    /**
     * 不等于 x != y
     */
    NEQ(1),
    /**
     * 包含 k like '%y%'
     */
    LIKE(2),
    /**
     * 左包含 x like '%y'
     */
    LF_LIKE(3),
    /**
     * 右包含 x like 'y%'
     */
    RT_LIKE(4),
    /**
     * 大于 x > y
     */
    GT(6),
    /**
     * 大于等于 x >= y
     */
    GT_EQ(7),
    /**
     * 小于 x < y
     */
    LT(8),
    /**
     * 小于等于 x <= y
     */
    LT_EQ(9),
    /**
     * 不包含 x not like '%y%'
     */
    NOT_LIKE(10),
    /**
     * 不左包含 x not like '%y'
     */
    NOT_LF_LIKE(11),
    /**
     * 不右包含 x not like 'y%'
     */
    NOT_RT_LIKE(12),
    /**
         * 区间 k between x and y
     */
    BETWEEN_AND(13),
    /**
     * 为空 k is null
     */
    NULL(14),
    /**
     * 不为空 k is not null
     */
    NOT_NULL(15),
    /**
     * 属于 x in (1, 2, 3)
     */
    IN(16),
    /**
     * 不属于 x not in (1, 2, 3)
     */
    NOT_IN(17);

    private int code;

    Operator(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public static Operator get(int code) {
        for(Operator o : Operator.values()) {
            if(o.code == code) {
                return o;
            }
        }

        throw new RuntimeException("#Operator #get() enum item not found: code - " + code);
    }
}
