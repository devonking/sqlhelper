package com.devonking.sql.pie.simple;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.ColumnValuePair;
import com.devonking.sql.pie.core.Sets;
import com.devonking.sql.pie.core.Table;
import com.devonking.sql.pie.statement.Insert;
import com.devonking.sql.mybatis.SqlMapper;
import com.devonking.sql.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleInsert implements Insert {
    private static final Logger log = LoggerFactory.getLogger(SimpleInsert.class);

    private Table table;

    private Sets sets;

    private Map<String, Object> preparedValues = new HashMap<>(16);

    @Override
    public Insert sets(Sets sets) {
        this.sets = sets;
        return this;
    }

    @Override
    public Integer fire(SqlMapper mapper) {
        Map<String, Object> params = preparedValues;
        params.put(Constant.RESERVED_KEYWORD_SQL, shout());
        return mapper.insert(params);
    }

    @Override
    public <T extends Serializable> T fire(SqlMapper mapper, Class<T> entityIdClazz) {
        Map<String, Object> params = preparedValues;
        params.put(Constant.RESERVED_KEYWORD_SQL, shout());
        mapper.insert(params);
        return (T) params.get(Constant.RESERVED_KEYWORD_AUTO_ID);
    }

    @Override
    public Insert insert(String name) {
        return insert(name, null);
    }

    @Override
    public Insert insert(String name, String alias) {
        return insert(new Table(name, alias));
    }

    @Override
    public Insert insert(Table table) {
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
                .append(") VALUES (")
                .append(buildValues())
                .append(")");

        if(log.isDebugEnabled()) {
            log.debug(StringUtils.format(preparedValues));
        }

        return sql.toString();
    }

    private String buildTable() {
        return table.getName();
    }

    private String buildColumns() {
        StringBuilder columns = new StringBuilder();
        if(null != sets && null != sets.getColumnValuePairs()) {
            sets.getColumnValuePairs().forEach(cv -> columns.append(cv.getColumn()).append(Constant.DELIMITER_COMMA));
            StringUtils.trimTail(columns, Constant.DELIMITER_COMMA);
        }
        return columns.toString();
    }

    private String buildValues() {
        if(null != sets && null != sets.getColumnValuePairs()) {
            int counter = 1;
            StringBuilder values = new StringBuilder();
            for (ColumnValuePair cv : sets.getColumnValuePairs()) {
                values.append("#{").append(placeholder(counter ++, cv.getValue())).append("}").append(Constant.DELIMITER_COMMA);
            }
            StringUtils.trimTail(values, Constant.DELIMITER_COMMA);
            return values.toString();
        }
        return "";
    }

    private String placeholder(int index, Object value) {
        String ph = "i" + index;
        preparedValues.put(ph, value);
        return ph;
    }
}
