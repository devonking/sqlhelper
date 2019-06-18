package com.devonking.sql.mybatis;

import com.devonking.sql.util.ObjectUtil;
import com.devonking.sql.constant.Constant;
import com.devonking.sql.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class SqlBuilder {
    private static final Logger log = LoggerFactory.getLogger(SqlBuilder.class);

    /**
     * 构建执行SQL，并日志输入参数
     *
     * @param params
     * @return
     */
    public static String buildSQL(Map<String, Object> params) {
        String sql = getSQL(params);
        log.info(sql);

        log.info(format(params));
        return sql;
    }

    public static String buildDdlSQL(String ddlSql) {
        log.info(ddlSql);
        return ddlSql;
    }

    /**
     * 获取待执行SQL
     *
     * @param params
     * @return
     */
    private static String getSQL(Map<String, Object> params) {
        String sql = (String) params.get(Constant.RESERVED_KEYWORD_SQL);
        if(ObjectUtil.blank(sql)) {
            throw new RuntimeException("#SQL builder - SQL cannot be empty");
        }

        return sql;
    }

    /**
     * 格式化参数
     *
     * @param params
     * @return
     */
    private static String format(Map<String, Object> params) {
        Object sql = params.get(Constant.RESERVED_KEYWORD_SQL);
        if (Objects.nonNull(sql)) {
            params.remove(Constant.RESERVED_KEYWORD_SQL);
        }
        String format = StringUtils.format(params);
        if (Objects.nonNull(sql)) {
            params.put(Constant.RESERVED_KEYWORD_SQL, sql);
        }

        return format;
    }
}
