package com.devonking.sql.pie.ability;

import com.devonking.sql.pie.core.Conditions;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Filterable<T> {

    /**
     * Filter records
     *
     * @param conditions
     * @return
     */
    T filter(Conditions conditions);
}
