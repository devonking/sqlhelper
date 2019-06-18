package com.devonking.sql.pie.ability;

import com.devonking.sql.pie.core.Table;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Fromable<T> {

    /**
     * Specify table
     *
     * @param name
     * @return
     */
    T from(String name);

    /**
     * Specify table
     *
     * @param name
     * @param alias
     * @return
     */
    T from(String name, String alias);

    /**
     * Specify table
     *
     * @param table
     * @return
     */
    T from(Table table);
}
