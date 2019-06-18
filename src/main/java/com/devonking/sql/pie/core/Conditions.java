package com.devonking.sql.pie.core;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.filter.FilterHelper;
import com.devonking.sql.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class Conditions {

    private FilterHelper filter;

    private List<Conditions> andRolls;

    private List<Conditions> orRolls;

    public List<Conditions> getAndRolls() {
        return this.andRolls;
    }

    public List<Conditions> getOrRolls() {
        return this.orRolls;
    }

    public FilterHelper getFilter() {
        return filter;
    }

    public void setFilter(FilterHelper filter) {
        this.filter = filter;
    }

    public static Conditions pie() {
        Conditions conditions = new Conditions();
        conditions.setFilter(new FilterHelper(true, false));
        return conditions;
    }

    public Conditions andRoll(Conditions conditions) {
        if(null == andRolls) {
            andRolls = new ArrayList<>();
        }
        andRolls.add(conditions);
        return this;
    }

    public Conditions orRoll(Conditions conditions) {
        if(null == orRolls) {
            orRolls = new ArrayList<>();
        }
        orRolls.add(conditions);
        return this;
    }

    public Conditions andEq(String key, Object value) {
        this.filter.and(key, value);
        return this;
    }

    public Conditions orEq(String key, Object value) {
        this.filter.or(key, value);
        return this;
    }

    public String toSql() {
        StringBuilder s = new StringBuilder();
        if(null != filter) {
            s.append(filter.sql(false)).append(Constant.DELIMITER_SINGLE_SPACE);
        }
        if(null != andRolls) {
            andRolls.forEach(ar -> {
                s.append("AND ").append(ar.toSql());
            });
        }
        if(null != orRolls) {
            orRolls.forEach(or -> {
                s.append("OR ").append(or.toSql());
            });
        }
        StringUtils.trimTail(s, Constant.DELIMITER_SINGLE_SPACE);
        return s.toString();
    }

    public Map<String, Object> values() {
        Map<String, Object> values = new HashMap<>(16);
        if(null != filter) {
            values.putAll(filter.values());
        }
        if(null != andRolls) {
            andRolls.forEach(ar -> {
               values.putAll(ar.values());
            });
        }
        if(null != orRolls) {
            orRolls.forEach(or -> {
                values.putAll(or.values());
            });
        }
        return values;
    }
}
