package com.devonking.sql.pie.core;

import com.devonking.sql.util.ObjectUtil;
import com.devonking.sql.constant.Constant;
import com.devonking.sql.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class Bys {

    private Set<OrderByPair> orderByPairs = new LinkedHashSet<>();

    private Bys() {}

    public static Bys pie() {
        return new Bys();
    }

    public Bys asc(String... orders) {
        for(String order : orders) {
            orderByPairs.add(new OrderByPair(order, OrderType.ASC));
        }
        return this;
    }

    public Bys desc(String... orders) {
        for(String order : orders) {
            orderByPairs.add(new OrderByPair(order, OrderType.DESC));
        }
        return this;
    }

    public String toSql() {
        if(ObjectUtil.empty(orderByPairs)) {
            return "";
        } else {
            StringBuilder sql = new StringBuilder();
            orderByPairs.forEach(o -> sql.append(o.getOrder()).append(Constant.DELIMITER_SINGLE_SPACE).append(o.getType()).append(Constant.DELIMITER_COMMA));
            StringUtils.trimTail(sql, Constant.DELIMITER_COMMA);
            return sql.toString();
        }
    }
}
