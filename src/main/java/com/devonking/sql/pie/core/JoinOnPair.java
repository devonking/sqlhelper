package com.devonking.sql.pie.core;

import com.devonking.sql.pie.statement.Join;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class JoinOnPair {

    private Join join;

    private Conditions on;

    public JoinOnPair(Join join, Conditions on) {
        this.join = join;
        this.on = on;
    }

    public Join getJoin() {
        return join;
    }

    public void setJoin(Join join) {
        this.join = join;
    }

    public Conditions getOn() {
        return on;
    }

    public void setOn(Conditions on) {
        this.on = on;
    }
}
