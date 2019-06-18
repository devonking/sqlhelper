package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.ability.Fromable;
import com.devonking.sql.pie.ability.Shoutable;
import com.devonking.sql.pie.core.Statement;
import com.devonking.sql.mybatis.SqlMapper;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Rename extends Statement, Fromable<Rename>, Shoutable {

    /**
     * Specify new table name
     *
     * @param newName
     * @return
     */
    Rename to(String newName);

    /**
     * Execute SQL statement
     *
     * @param mapper
     */
    void fire(SqlMapper mapper);
}
