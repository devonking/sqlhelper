package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.ability.Insertable;

public interface InsertSelect extends Insertable<InsertSelect> {

    InsertSelect select(Select select);
}
