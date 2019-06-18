package com.devonking.sql.pie.core;

import java.util.ArrayList;

public class ResultList<E> extends ArrayList<E> {
    public E one() {
        if (size() > 0) {
            return get(0);
        } else {
            return null;
        }
    }
}
