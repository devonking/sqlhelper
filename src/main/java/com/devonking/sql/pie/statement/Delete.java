package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.ability.Executable;
import com.devonking.sql.pie.ability.Filterable;
import com.devonking.sql.pie.ability.Fromable;
import com.devonking.sql.pie.ability.Shoutable;
import com.devonking.sql.pie.core.Statement;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Delete extends Statement, Fromable<Delete> , Filterable<Delete>, Executable, Shoutable {
}
