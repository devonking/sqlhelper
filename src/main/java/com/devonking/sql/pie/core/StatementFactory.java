package com.devonking.sql.pie.core;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface StatementFactory {

    /**
     * Get statement type
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends Statement> T getStatement(Class<T> clazz);
}
