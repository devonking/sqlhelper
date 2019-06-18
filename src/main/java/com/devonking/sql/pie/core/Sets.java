package com.devonking.sql.pie.core;

import java.util.*;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class Sets {
    /**
     * 字段与字段值集合
     */
    private Set<ColumnValuePair> columnValuePairs = new LinkedHashSet<>();


    private Sets() {}

    public static Sets pie(){
        return new Sets();
    }

    public Set<ColumnValuePair> getColumnValuePairs() {
        return this.columnValuePairs;
    }

    /**
     * 更新或插入时，设置字段和字段值
     *
     * @param column
     * @param value
     * @return
     */
    public Sets set(String column, Object value) {
        this.columnValuePairs.add(new ColumnValuePair(column, value));
        return this;
    }

    /**
     * 更新或插入时，设置字段和字段值
     *
     * @param columnValueMap
     * @return
     */
    public Sets set(Map<String, Object> columnValueMap) {
        columnValueMap.forEach((k, v) -> this.columnValuePairs.add(new ColumnValuePair(k, v)));
        return this;
    }

    /**
     * 更新或插入时，设置字段和字段值
     *
     * @param columnValuePairs
     * @return
     */
    public Sets set(ColumnValuePair... columnValuePairs) {
        this.columnValuePairs.addAll(Arrays.asList(columnValuePairs));
        return this;
    }

    /**
     * 更新或插入时，设置字段和字段值
     *
     * @param columnValuePairs
     * @return
     */
    public Sets set(List<ColumnValuePair> columnValuePairs) {
        this.columnValuePairs.addAll(columnValuePairs);
        return this;
    }
}
