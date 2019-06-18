package com.devonking.sql.pie.simple;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.*;
import com.devonking.sql.pie.statement.Join;
import com.devonking.sql.pie.statement.Update;
import com.devonking.sql.mybatis.SqlMapper;
import com.devonking.sql.util.LangUtils;
import com.devonking.sql.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleUpdate implements Update {
    private static final Logger log = LoggerFactory.getLogger(SimpleUpdate.class);

    private Table table;

    private Sets sets;

    private Conditions filters;

    private List<JoinOnPair> joinOnBucket;

    private Map<String, Object> preparedValues = new HashMap<>(16);

    @Override
    public Update set(Sets sets) {
        this.sets = sets;
        return this;
    }

    @Override
    public Integer fire(SqlMapper mapper) {
        String sql = shout();
        Map<String, Object> params = getParams();
        params.put(Constant.RESERVED_KEYWORD_SQL, sql);
        return mapper.update(params);
    }

    @Override
    public Update filter(Conditions conditions) {
        this.filters = conditions;
        return this;
    }

    @Override
    public Update update(String name) {
        return update(name, null);
    }

    @Override
    public Update update(String name, String alias) {
        return update(new Table(name, alias));
    }

    @Override
    public Update update(Table table) {
        this.table = table;
        return this;
    }

    @Override
    public Update join(Join join) {
        if(null == joinOnBucket) {
            joinOnBucket = new ArrayList<>();
        }
        joinOnBucket.add(new JoinOnPair(join, null));
        return this;
    }

    @Override
    public Update on(Conditions conditions) {
        if(null == joinOnBucket || joinOnBucket.size() == 0) {
            // throw error
        }
        JoinOnPair joinOnPair = joinOnBucket.get(joinOnBucket.size() - 1);
        joinOnPair.setOn(conditions);
        return this;
    }

    @Override
    public String shout() {
        StringBuilder sql = new StringBuilder();
        if(null != joinOnBucket && joinOnBucket.size() > 0) {
            sql
                    .append("UPDATE")
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append(buildFrom())
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append(buildJoinOn())
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append("SET")
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append(buildSets())
                    .append(buildWhere());
        } else {
            sql
                    .append("UPDATE")
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append(buildTable())
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append("SET")
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append(buildSets())
                    .append(buildWhere());
        }

        if(log.isDebugEnabled()) {
            log.debug(StringUtils.format(getParams()));
        }

        return sql.toString();
    }

    private Map<String, Object> getParams() {
        Map<String, Object> allParams = new HashMap<>(16);
        if (null != filters) {
            allParams.putAll(filters.values());
        }

        Map<String, Object> params = new HashMap<>(allParams.size() >> 2);
        if (null != joinOnBucket) {
            joinOnBucket.forEach(j -> {
                params.putAll(j.getOn().values());
            });
        }
        allParams.putAll(params);
        allParams.putAll(preparedValues);
        return allParams;
    }

    private String buildTable() {
        return table.toSql();
    }

    private String buildFrom() {
        return "FROM " + table.toSql();
    }

    private String buildSets() {
        if(null != sets && null != sets.getColumnValuePairs()) {
            int counter = 1;
            StringBuilder s = new StringBuilder();
            for (ColumnValuePair cv : sets.getColumnValuePairs()) {
                s.append(cv.getColumn());
                if(cv.getValue() instanceof String && LangUtils.isVar((String) cv.getValue())) {
                    s.append("= ").append(LangUtils.var((String) cv.getValue()));
                } else {
                    s.append("= #{").append(placeholder(counter++, cv.getValue())).append("}");
                }
                s.append(Constant.DELIMITER_COMMA);
            }
            StringUtils.trimTail(s, Constant.DELIMITER_COMMA);
            return s.toString();
        }
        return "";
    }

    private String placeholder(int index, Object value) {
        String p = "u" + index;
        preparedValues.put(p, value);
        return p;
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
        }
        StringUtils.trimTail(s, Constant.DELIMITER_SINGLE_SPACE);
        return s.toString();
    }

    private String buildWhere() {
        if(null != filters) {
            return " WHERE " + filters.toSql();
        }
        return "";
    }
}
