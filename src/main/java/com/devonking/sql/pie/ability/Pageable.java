package com.devonking.sql.pie.ability;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Pageable<T> {

    /**
     * Pagination
     *
     * @param num
     * @param size
     * @return
     */
    T page(int num, int size);
}
