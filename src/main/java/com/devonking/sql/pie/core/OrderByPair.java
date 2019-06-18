package com.devonking.sql.pie.core;

import com.devonking.sql.util.ObjectUtil;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class OrderByPair {

    private String order;

    private OrderType type;

    public OrderByPair(String order, OrderType type) {
        this.order = order;
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if(null != order) {
            result = 31 * result + order.hashCode();
        }
        if(null != type) {
            result = 31 * result + type.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(obj instanceof OrderByPair) {
            OrderByPair o = (OrderByPair) obj;
            return ObjectUtil.equal(order, o.getOrder()) && ObjectUtil.equal(type, o.getType());
        }

        return false;
    }
}
