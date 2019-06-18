package com.devonking.sql.pie.core;

import com.devonking.sql.util.ObjectUtil;
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
public class ColumnAliasPair {

    @Getter
    private String column;

    @Getter
    private String alias;

    @Override
    public int hashCode() {
        int result = 17;
        if(null != column) {
            result = 31 * result + column.hashCode();
        }
        if(null != alias) {
            result = 31 * result + alias.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(obj instanceof ColumnAliasPair) {
            ColumnAliasPair o = (ColumnAliasPair) obj;
            return ObjectUtil.equal(column, o.getColumn()) && ObjectUtil.equal(alias, o.getAlias());
        }

        return false;
    }

    public String toSql() {
        return (ObjectUtil.notBlank(alias)? column + " AS `" + alias + "`": column);
    }
}
