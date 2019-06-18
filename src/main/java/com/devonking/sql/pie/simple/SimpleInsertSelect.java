package com.devonking.sql.pie.simple;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.*;
import com.devonking.sql.pie.statement.InsertSelect;
import com.devonking.sql.pie.statement.Select;
import com.devonking.sql.mybatis.SqlMapper;
import com.devonking.sql.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleInsertSelect implements InsertSelect {

    private Table table;

    private Select select;

    @Override
    public SimpleInsertSelect select(Select select) {
        this.select = select;
        return this;
    }

    @Override
    public Integer fire(SqlMapper mapper) {
        Map<String, Object> params = getParams();
        params.put(Constant.RESERVED_KEYWORD_SQL, shout());
        return mapper.insert(params);
    }

    @Override
    public SimpleInsertSelect insert(String name) {
        return insert(name, null);
    }

    @Override
    public SimpleInsertSelect insert(String name, String alias) {
        return insert(new Table(name, alias));
    }

    @Override
    public SimpleInsertSelect insert(Table table) {
        this.table = table;
        return this;
    }

    @Override
    public String shout() {
        StringBuilder sql = new StringBuilder();
        sql
                .append("INSERT INTO")
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append(buildTable())
                .append("(")
                .append(buildColumns())
                .append(")")
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append(buildSelect());

        return sql.toString();
    }

    private String buildTable() {
        return table.getName();
    }

    private String buildColumns() {
        StringBuilder columnsStr = new StringBuilder();
        Columns selectColumns = this.select.getColumns();
        if (null != selectColumns) {
            selectColumns.getColumnAliasPairs()
                    .forEach(c -> columnsStr.append(c.getAlias()).append(Constant.DELIMITER_COMMA));
            StringUtils.trimTail(columnsStr, Constant.DELIMITER_COMMA);
        } else {
            throw new RuntimeException("#SimpleInsertSelect:: build columns failed, sets are empty");
        }
        return columnsStr.toString();
    }

    private String buildSelect() {
        return select.shout();
    }

    private Map<String, Object> getParams() {
        Map<String, Object> allParams = new HashMap<>(16);
        Conditions filters = select.getConditions();
        if(null != filters) {
            allParams.putAll(filters.values());
        }

        Map<String, Object> params = new HashMap<>(allParams.size() >> 2);
        List<JoinOnPair> joinOnBucket = select.getJoinOnBucket();
        if(null != joinOnBucket) {
            joinOnBucket.forEach(j -> {
                params.putAll(j.getOn().values());
            });
        }
        allParams.putAll(params);
        return allParams;
    }
}
