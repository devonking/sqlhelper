package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.ability.Insertable;
import com.devonking.sql.pie.core.Sets;
import com.devonking.sql.mybatis.SqlMapper;

import java.io.Serializable;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Insert extends Insertable<Insert> {

    /**
     * Specify values
     *
     * @param sets
     * @return
     */
    Insert sets(Sets sets);

    /**
     * Execute SQL statement
     *
     * @param mapper
     * @param entityIdClazz
     * @param <T>
     * @return
     */
    <T extends Serializable> T fire(SqlMapper mapper, Class<T> entityIdClazz);
}
