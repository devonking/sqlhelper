package com.devonking.sql.pie.simple;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.Sets;
import com.devonking.sql.pie.statement.Native;
import com.devonking.sql.mybatis.SqlMapper;
import com.devonking.sql.util.LangUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SimpleNative implements Native {

    private String nativeSql;

    private Sets sets;

    @Override
    public Native load(String nativeSql) {
        this.nativeSql = nativeSql.trim();
        return this;
    }

    @Override
    public Native sets(Sets sets) {
        this.sets = sets;
        return this;
    }

    @Override
    public <T> T fire(SqlMapper mapper) {
        String sql = nativeSql.substring(0, 10).toUpperCase();
        if(sql.startsWith(Constant.KEYWORD_SELECT)) {
            return (T) mapper.query(getParams());
        }

        if(sql.startsWith(Constant.KEYWORD_INSERT)) {
            return (T) mapper.insert(getParams());
        }

        if(sql.startsWith(Constant.KEYWORD_UPDATE)) {
            return (T) mapper.update(getParams());
        }

        if(sql.startsWith(Constant.KEYWORD_DELETE)) {
            return (T) mapper.delete(getParams());
        }

        return null;
    }

    @Override
    public <T extends Serializable> T fire(SqlMapper mapper, Class<T> entityIdClazz) {
        String sql = nativeSql.substring(0, 10).toUpperCase();
        if(sql.startsWith(Constant.KEYWORD_INSERT)) {
            Map<String, Object> params = getParams();
            mapper.insert(getParams());
            return (T) params.get(Constant.RESERVED_KEYWORD_AUTO_ID);
        }

        return null;
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params;
        if(null != sets && null != sets.getColumnValuePairs()) {
            params = new HashMap<>(16);
            sets.getColumnValuePairs().forEach(cv -> {
                params.put(cv.getColumn(), parseValue(cv.getValue()));
            });
            params.put(Constant.RESERVED_KEYWORD_SQL, nativeSql);
            return params;
        } else {
            params = new HashMap<>(1);
            params.put(Constant.RESERVED_KEYWORD_SQL, nativeSql);
            return params;
        }
    }

    private Object parseValue(Object value) {
        if(value instanceof String && LangUtils.isVar((String) value)) {
            return LangUtils.var((String) value);
        } else {
            return value;
        }
    }
}
