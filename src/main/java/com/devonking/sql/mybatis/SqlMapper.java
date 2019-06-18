package com.devonking.sql.mybatis;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.core.ResultList;
import org.apache.ibatis.annotations.*;

import java.util.Map;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
@Mapper
public interface SqlMapper {

    /**
     * 动态查询
     *
     * @param params
     * @return
     */
    @SelectProvider(type = SqlBuilder.class, method = "buildSQL")
    ResultList<Map<String, Object>> query(Map<String, Object> params);

    /**
     * 动态插入
     *
     * @param params
     * @return
     */
    @SelectKey(keyColumn = Constant.RESERVED_KEYWORD_AUTO_ID, keyProperty = Constant.RESERVED_KEYWORD_AUTO_ID,
            resultType = long.class, before = false, statement = "SELECT LAST_INSERT_ID() AS ID")
    @InsertProvider(type = SqlBuilder.class, method = "buildSQL")
    Integer insert(Map<String, Object> params);

    /**
     * 动态更新
     *
     * @param params
     * @return
     */
    @UpdateProvider(type = SqlBuilder.class, method = "buildSQL")
    Integer update(Map<String, Object> params);

    /**
     * 动态删除
     *
     * @param params
     * @return
     */
    @DeleteProvider(type = SqlBuilder.class, method = "buildSQL")
    Integer delete(Map<String, Object> params);

    /**
     * 动态更新结构
     *
     * @param ddlSql
     * @return
     */
    @UpdateProvider(type = SqlBuilder.class, method = "buildDdlSQL")
    void struct(String ddlSql);
}
