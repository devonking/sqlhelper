package com.devonking.sql.pie.core;

import com.devonking.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
@Builder
@AllArgsConstructor
public class ColumnValuePair {

    @Getter
    private String column;

    @Getter
    private Object value;

    @Override
    public int hashCode() {
        int result = 17;
        if(null != column) {
            result = 31 * result + column.hashCode();
        }
        if(null != value) {
            result = 31 * result + value.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(obj instanceof ColumnValuePair) {
            ColumnValuePair o = (ColumnValuePair) obj;
            return ObjectUtil.equal(column, o.getColumn()) && ObjectUtil.equal(value, o.getValue());
        }

        return false;
    }

    public String toSql() {
        return column + " = " + value;
    }
}
