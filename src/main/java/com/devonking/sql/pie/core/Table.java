package com.devonking.sql.pie.core;

import com.devonking.ObjectUtil;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class Table {

    private String name;

    private String alias;

    public Table() {

    }

    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String toSql() {
        if(ObjectUtil.blank(alias)) {
            return name;
        } else {
            return name + " " + alias;
        }
    }
}
