package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.core.Sets;
import com.devonking.sql.pie.core.Statement;
import com.devonking.sql.mybatis.SqlMapper;

import java.io.Serializable;


/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Native extends Statement {

    /**
     * Specify SQL statement
     *
     * @param nativeSql
     * @return
     */
    Native load(String nativeSql);

    /**
     * Specify values
     *
     * @param sets
     * @return
     */
    Native sets(Sets sets);

    /**
     * Execute SQL statement
     *
     * @param mapper
     * @param <T>
     * @return
     */
    <T> T fire(SqlMapper mapper);

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
