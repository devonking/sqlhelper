package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.ability.Tableable;
import com.devonking.sql.pie.core.Columns;
import com.devonking.sql.pie.core.JoinType;
import com.devonking.sql.pie.core.Statement;
import com.devonking.sql.pie.core.Table;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Join extends Statement, Tableable<Join> {

    /**
     * Specify columns to be returned
     *
     * @param columns
     * @return
     */
    Join query(Columns columns);

    /**
     * Get columns
     *
     * @return
     */
    Columns getColumns();

    /**
     * Get table
     *
     * @return
     */
    Table getTable();

    /**
     * Get type
     *
     * @return
     */
    JoinType getType();
}
