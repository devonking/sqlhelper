package com.devonking.sql.pie.ability;

import com.devonking.sql.pie.core.Table;

public interface Tableable<T> {

    /**
     * Specify table
     *
     * @param name
     * @return
     */
    T table(String name);

    /**
     * Specify table
     *
     * @param name
     * @param alias
     * @return
     */
    T table(String name, String alias);

    /**
     * Specify table
     *
     * @param table
     * @return
     */
    T table(Table table);
}
