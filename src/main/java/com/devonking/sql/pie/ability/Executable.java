package com.devonking.sql.pie.ability;

import com.devonking.sql.mybatis.SqlMapper;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Executable {

    /**
     * Execute SQL statement
     *
     * @param mapper
     * @return
     */
    Integer fire(SqlMapper mapper);
}
