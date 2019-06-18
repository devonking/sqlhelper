package com.devonking.sql.pie.core;

import java.lang.reflect.Field;
import java.util.*;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class Columns {
    /**
     * 字段与字段别名集合
     */
    private Set<ColumnAliasPair> columnAliasPairs = new LinkedHashSet<>();

    private Columns() {}

    public static Columns pie() {
        return new Columns();
    }

    public static <T> Columns pie(Class<T> clazz) {
        Columns instance = new Columns();
        instance.columnAliasPairs = buildColumnAliasPairs(clazz);
        return instance;
    }

    public Set<ColumnAliasPair> getColumnAliasPairs() {
        return this.columnAliasPairs;
    }

    /**
     * 添加查询返回字段
     *
     * @param columns
     * @return
     */
    public Columns get(String... columns) {
        for(String c : columns) {
            this.columnAliasPairs.add(new ColumnAliasPair(c, c));
        }
        return this;
    }

    /**
     * 添加查询返回字段
     *
     * @param columns
     * @return
     */
    public Columns get(List<String> columns) {
        columns.forEach(c -> this.columnAliasPairs.add(new ColumnAliasPair(c, c)));
        return this;
    }

    /**
     * 添加查询返回字段
     *
     * @param columnAliasMap
     * @return
     */
    public Columns get(Map<String, String> columnAliasMap) {
        columnAliasMap.forEach((k, v) -> this.columnAliasPairs.add(new ColumnAliasPair(k, v)));
        return this;
    }

    /**
     * 添加查询返回字段
     *
     * @param columnAliasPairs
     * @return
     */
    public Columns get(ColumnAliasPair... columnAliasPairs) {
        this.columnAliasPairs.addAll(Arrays.asList(columnAliasPairs));
        return this;
    }

    /**
     * 解析对象生成字段对
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> Set<ColumnAliasPair> buildColumnAliasPairs(Class<T> clazz){
        if(null == clazz){
            return null;
        }

        Set<ColumnAliasPair> cap = new LinkedHashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if (field.isAnnotationPresent(Piece.class)) {
                Piece piece = field.getAnnotation(Piece.class);
                String column = piece.value();
                String alias = piece.alias();
                if (null == alias || "".equals(alias.trim())) {
                    alias = field.getName();
                }
                cap.add(new ColumnAliasPair(column, alias));
            }
        }

        /** 处理父类字段**/
        Class<?> superClass = clazz.getSuperclass();
        if(superClass.equals(Object.class)) {
            return cap;
        }

       cap.addAll(buildColumnAliasPairs(superClass));
        return cap;
    }
}
