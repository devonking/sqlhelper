package com.devonking.sql.pie.simple;

import com.github.pagehelper.PageHelper;
import com.devonking.ObjectUtil;
import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.*;
import com.devonking.sql.pie.core.ResultList;
import com.devonking.sql.pie.statement.Join;
import com.devonking.sql.pie.statement.Select;
import com.devonking.sql.mybatis.SqlMapper;
import com.devonking.sql.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleSelect implements Select {
    private static final Logger log = LoggerFactory.getLogger(SimpleSelect.class);

    private Table table;

    private Columns columns;

    private Conditions filters;

    private List<JoinOnPair> joinOnBucket;

    private Bys bys;

    private Page page;

    @Override
    public Table getTable() {
        return this.table;
    }

    @Override
    public Columns getColumns() {
        return this.columns;
    }

    @Override
    public Conditions getConditions() {
        return this.filters;
    }

    @Override
    public List<JoinOnPair> getJoinOnBucket() {
        return this.joinOnBucket;
    }

    @Override
    public Select query(Columns columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public ResultList<Map<String, Object>> fire(SqlMapper mapper) {
        Map<String, Object> params = getParams();
        params.put(Constant.RESERVED_KEYWORD_SQL, shout());
        if(null != page) {
            PageHelper.startPage(page.getNum(), page.getSize());
        }
        ResultList<Map<String, Object>> rltList = mapper.query(params);
        if(null != page) {
            PageHelper.clearPage();
        }
        return rltList;
    }

    @Override
    public <T> ResultList<T> fire(SqlMapper mapper, Class<T> entityClazz) {
        ResultList<Map<String, Object>> rltList = fire(mapper);
        if (ObjectUtil.empty(rltList)) {
            return new ResultList<>();
        }

        ResultList<T> rlt = new ResultList<>();
        rltList.forEach(row -> rlt.add(rowToEntity(row, entityClazz)));
        return rlt;
    }

    @Override
    public Map<String, Object> fireOne(SqlMapper mapper) {
        return fire(mapper).one();
    }

    @Override
    public <T> T fireOne(SqlMapper mapper, Class<T> entityClazz) {
        return fire(mapper, entityClazz).one();
    }

    @Override
    public Select filter(Conditions conditions) {
        this.filters = conditions;
        return this;
    }

    @Override
    public Select join(Join join) {
        if(null == joinOnBucket) {
            joinOnBucket = new ArrayList<>();
        }
        joinOnBucket.add(new JoinOnPair(join, null));
        return this;
    }

    @Override
    public Select on(Conditions conditions) {
        if(null == joinOnBucket || joinOnBucket.size() == 0) {
            // throw error
            throw new RuntimeException("#SQL.Pie#Select no join-table found");
        }
        JoinOnPair joinOnPair = joinOnBucket.get(joinOnBucket.size() - 1);
        joinOnPair.setOn(conditions);
        return this;
    }

    @Override
    public Select from(String name) {
        return from(name, null);
    }

    @Override
    public Select from(String name, String alias) {
        return from(new Table(name, alias));
    }

    @Override
    public Select from(Table table) {
        this.table = table;
        return this;
    }

    @Override
    public Select order(Bys bys) {
        this.bys = bys;
        return this;
    }

    @Override
    public Select page(int num, int size) {
        this.page = new Page(num, size);
        return this;
    }

    @Override
    public String shout() {
        StringBuilder sql = new StringBuilder();
        sql
                .append("SELECT")
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append(buildColumns())
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append(buildFrom())
                .append(buildJoinOn())
                .append(buildWhere())
                .append(buildOrderBy());

        if (log.isDebugEnabled()) {
            log.debug(StringUtils.format(getParams()));
        }

        return sql.toString();
    }

    private Map<String, Object> getParams() {
        Map<String, Object> allParams = new HashMap<>(16);
        if(null != filters) {
            allParams.putAll(filters.values());
        }

        Map<String, Object> params = new HashMap<>(allParams.size() >> 2);
        if(null != joinOnBucket) {
            joinOnBucket.forEach(j -> {
                params.putAll(j.getOn().values());
            });
        }
        allParams.putAll(params);
        return allParams;
    }

    private String buildColumns() {
        if(null == columns || null == columns.getColumnAliasPairs()) {
            return "*";
        }
        Columns allColumns = columns;
        if(null != joinOnBucket) {
            joinOnBucket.forEach(j -> {
                Columns c = j.getJoin().getColumns();
                if (null != c && null != c.getColumnAliasPairs()) {
                    Set<ColumnAliasPair> p = j.getJoin().getColumns().getColumnAliasPairs();
                    allColumns.get(p.toArray(new ColumnAliasPair[p.size()]));
                }
            });
        }
        StringBuilder columnSql = new StringBuilder();
        allColumns.getColumnAliasPairs().forEach(c -> columnSql.append(c.toSql()).append(Constant.DELIMITER_COMMA));
        StringUtils.trimTail(columnSql, Constant.DELIMITER_COMMA);
        return columnSql.toString();
    }

    private String buildFrom() {
        return "FROM " + table.toSql();
    }

    private String buildJoinOn() {
        StringBuilder s = new StringBuilder();
        if(null != joinOnBucket) {
            joinOnBucket.forEach(j -> {
                Join join = j.getJoin();
                Conditions conditions = j.getOn();

                s.append(join.getType())
                        .append(Constant.DELIMITER_SINGLE_SPACE)
                        .append("JOIN")
                        .append(Constant.DELIMITER_SINGLE_SPACE)
                        .append(join.getTable().toSql())
                        .append(Constant.DELIMITER_SINGLE_SPACE)
                        .append("ON")
                        .append(Constant.DELIMITER_SINGLE_SPACE)
                        .append(conditions.toSql())
                        .append(Constant.DELIMITER_SINGLE_SPACE);
            });
            StringUtils.trimTail(s, Constant.DELIMITER_SINGLE_SPACE);
            return Constant.DELIMITER_SINGLE_SPACE + s.toString();
        }
        return "";
    }

    private String buildWhere() {
        if(null != filters) {
            return Constant.DELIMITER_SINGLE_SPACE + "WHERE " + filters.toSql();
        }
        return "";
    }

    private String buildOrderBy() {
        if(null != bys) {
            return Constant.DELIMITER_SINGLE_SPACE + "ORDER BY " + bys.toSql();
        } else {
            return "";
        }
    }

    /**
     * 将查询结果Map转成实体对象
     *
     * @param row
     * @param entityClazz
     * @param <T>
     * @return 实体对象
     */
    private <T> T rowToEntity(Map<String, Object> row, Class<T> entityClazz) {
        T entity = null;
        try {
            entity = entityClazz.newInstance();
            mapToEntityPiece(entity, row, entityClazz);
        } catch (InstantiationException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            log.error("#SQL.Pie#Select failed to parse result row to entity object: {}", e.getMessage());
        }
        return entity;
    }

    /**
     * 将Map中的值赋值给实体对象上加了@Piece注解的属性
     *
     * @param entity 实体对象
     * @param map 值容器
     * @param clz 拥有Piece注解的类类型
     * @param <T> 实体泛型
     * @param <E> 拥有Piece注解的类泛型
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    private <T, E> void mapToEntityPiece(T entity, Map<String, Object> map, Class<E> clz) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Field[] fields = clz.getDeclaredFields();
        for(Field field : fields){
            if (field.isAnnotationPresent(Piece.class)) {
                Piece piece = field.getAnnotation(Piece.class);
                String alias = piece.alias();
                if (ObjectUtil.blank(alias)) {
                    alias = field.getName();
                }
                PropertyDescriptor property = new PropertyDescriptor(field.getName(), clz);
                if (null == property) {
                    log.error("#SQL.Pie#Select field '{}' does not have a Setter method", field.getName());
                } else {
                    property.getWriteMethod().invoke(entity, map.get(alias));
                }
            }
        }

        Class<?> superClz = clz.getSuperclass();
        if(superClz.equals(Object.class)) {
            return;
        }
        mapToEntityPiece(entity, map, superClz);
    }
}
