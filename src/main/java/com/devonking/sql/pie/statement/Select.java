package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.ability.*;
import com.devonking.sql.pie.core.*;
import com.devonking.sql.pie.core.ResultList;
import com.devonking.sql.mybatis.SqlMapper;

import java.util.List;
import java.util.Map;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Select extends Statement, Fromable<Select>, Filterable<Select>, Joinable<Select>, Pageable<Select>, Shoutable {

    /**
     * 设置查询字段
     *
     * @param columns
     * @return
     */
    Select query(Columns columns);

    /**
     * 获取直接查询的表对象
     *
     * @return
     */
    Table getTable();

    /**
     * 获取查询字段
     *
     * @return
     */
    Columns getColumns();

    /**
     * 获取查询条件
     *
     * @return
     */
    Conditions getConditions();

    /**
     * 获取关联对象
     *
     * @return
     */
    List<JoinOnPair> getJoinOnBucket();

    /**
     * 设置排序
     *
     * @param bys
     * @return
     */
    Select order(Bys bys);

    /**
     * 查询并返回一个记录集
     *
     * @param mapper
     * @return
     */
    ResultList<Map<String, Object>> fire(SqlMapper mapper);

    /**
     *  查询并返回一个实体记录集
     *
     * @param mapper
     * @param entityClazz 实体类型
     * @param <T> 实体类型
     * @return
     */
    <T> ResultList<T> fire(SqlMapper mapper, Class<T> entityClazz);

    /**
     * 查询并返回单条记录
     *
     * @param mapper
     * @return
     */
    Map<String, Object> fireOne(SqlMapper mapper);

    /**
     * 查询并返回单个实体
     *
     * @param mapper
     * @param entityClazz
     * @param <T>
     * @return
     */
    <T> T fireOne(SqlMapper mapper, Class<T> entityClazz);
}
