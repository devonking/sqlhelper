package com.devonking.sql.pie.simple;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.Conditions;
import com.devonking.sql.pie.core.Table;
import com.devonking.sql.pie.statement.Delete;
import com.devonking.sql.mybatis.SqlMapper;
import com.devonking.sql.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleDelete implements Delete {
    private static final Logger log = LoggerFactory.getLogger(SimpleDelete.class);

    private Table table;

    private Conditions filters;

    @Override
    public Integer fire(SqlMapper mapper) {
        Map<String, Object> params = getParams();
        params.put(Constant.RESERVED_KEYWORD_SQL, shout());
        return mapper.delete(params);
    }

    @Override
    public Delete filter(Conditions conditions) {
        this.filters = conditions;
        return this;
    }

    @Override
    public Delete from(String name) {
        return from(name, null);
    }

    @Override
    public Delete from(String name, String alias) {
        return from(new Table(name, alias));
    }

    @Override
    public Delete from(Table table) {
        this.table = table;
        return this;
    }

    @Override
    public String shout() {
        StringBuilder sql = new StringBuilder();
        sql
                .append("DELETE FROM")
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append(buildTable())
                .append(buildWhere());

        if(log.isDebugEnabled()) {
            log.debug(StringUtils.format(getParams()));
        }

        return sql.toString();
    }

    private Map<String, Object> getParams() {
        return filters.values();
    }

    private String buildTable() {
        return table.getName();
    }

    private String buildWhere() {
        if(null != filters) {
            return " WHERE " + filters.toSql();
        }
        return "";
    }
}
