package com.devonking.sql.pie.ability;


import com.devonking.sql.pie.core.Conditions;
import com.devonking.sql.pie.statement.Join;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Joinable<T> {

    /**
     * Join table
     *
     * @param join
     * @return
     */
    T join(Join join);

    /**
     * Join criteria
     *
     * @param conditions
     * @return
     */
    T on(Conditions conditions);
}
