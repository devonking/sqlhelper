package com.devonking.sql.pie.ability;

import com.devonking.sql.pie.core.Statement;
import com.devonking.sql.pie.core.Table;

public interface Insertable<T> extends Statement, Executable, Shoutable {
    /**
     * Specify table
     *
     * @param name
     * @return
     */
    T insert(String name);

    /**
     * Specify table
     *
     * @param name
     * @param alias
     * @return
     */
    T insert(String name, String alias);

    /**
     * Specify table
     *
     * @param table
     * @return
     */
    T insert(Table table);
}
