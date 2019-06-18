package com.devonking.sql.pie.simple;

import com.devonking.sql.pie.core.Columns;
import com.devonking.sql.pie.core.JoinType;
import com.devonking.sql.pie.core.Table;
import com.devonking.sql.pie.statement.Join;
import com.devonking.sql.pie.statement.RightJoin;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleRightJoin implements RightJoin {

    private Table table;

    private Columns columns;

    @Override
    public Join query(Columns columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public Columns getColumns() {
        return this.columns;
    }

    @Override
    public Table getTable() {
        return this.table;
    }

    @Override
    public JoinType getType() {
        return JoinType.RIGHT;
    }

    @Override
    public Join table(String name) {
        return table(name, null);
    }

    @Override
    public Join table(String name, String alias) {
        return table(new Table(name, alias));
    }

    @Override
    public Join table(Table table) {
        this.table = table;
        return this;
    }
}
