package com.devonking.sql.pie.simple;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.Table;
import com.devonking.sql.pie.statement.Drop;
import com.devonking.sql.mybatis.SqlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleDrop implements Drop {
    private static final Logger log = LoggerFactory.getLogger(SimpleDrop.class);

    private Table table;

    @Override
    public void fire(SqlMapper mapper) {
        mapper.struct(shout());
    }

    @Override
    public Drop from(String name) {
        return from(name, null);
    }

    @Override
    public Drop from(String name, String alias) {
        return from(new Table(name, alias));
    }

    @Override
    public Drop from(Table table) {
        this.table = table;
        return this;
    }

    @Override
    public String shout() {
        StringBuilder sql = new StringBuilder();
        sql
                .append("DROP TABLE")
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append(buildTable());

        return sql.toString();
    }

    private String buildTable() {
        return table.getName();
    }
}
