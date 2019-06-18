package com.devonking.sql.pie.simple;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.Table;
import com.devonking.sql.pie.statement.Rename;
import com.devonking.sql.mybatis.SqlMapper;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleRename implements Rename {

    private Table table;

    private String newName;

    @Override
    public Rename to(String newName) {
        this.newName = newName;
        return this;
    }

    @Override
    public void fire(SqlMapper mapper) {
        mapper.struct(shout());
    }

    @Override
    public Rename from(String name) {
        return from(name, null);
    }

    @Override
    public Rename from(String name, String alias) {
        return from(new Table(name, alias));
    }

    @Override
    public Rename from(Table table) {
        this.table = table;
        return this;
    }

    @Override
    public String shout() {
        StringBuilder sql = new StringBuilder();
        sql
                .append("RENAME TABLE")
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append(buildTable())
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append("TO")
                .append(Constant.DELIMITER_SINGLE_SPACE)
                .append(buildNewTableName());

        return sql.toString();
    }

    private String buildTable() {
        return "`" + table.getName() + "`";
    }

    private String buildNewTableName() {
        return "`" + newName + "`";
    }
}
