package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.ability.*;
import com.devonking.sql.pie.core.Sets;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Update extends Updateable<Update>, Joinable<Update>, Filterable<Update> {

    /**
     * Specify values
     *
     * @param sets
     * @return
     */
    Update set(Sets sets);
}
