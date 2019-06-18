package com.devonking.sql.pie.statement;

import com.devonking.sql.pie.ability.Shoutable;
import com.devonking.sql.pie.ability.Tableable;
import com.devonking.sql.pie.core.ColumnDefinition;
import com.devonking.sql.pie.core.Statement;
import com.devonking.sql.mybatis.SqlMapper;

import java.util.List;

/**
 * description in here
 *
 *
 * ALTER TABLE xxx
 * ADD COLUMN yyy varchar(100) not null default '0' comment 'zzz',
 * CHANGE OLD_COLUMN NEW_COLUMN yyy varchar(50) not null default '0' comment 'ttt',
 * MODIFY COLUMN yyy varchar(50) not null default '0' comment 'ttt',
 * DROP COLUMN yyy;
 *
 * @author Devon King
 * @since 1.0
 */
public interface AlterColumn extends Statement, Tableable<AlterColumn>, Shoutable {

    AlterColumn add(ColumnDefinition... columnDefinitions);

    AlterColumn add(List<ColumnDefinition> columnDefinitions);

    /**
     * 部分修改字段信息
     * 比如： 只设置了 ColumnDefinition.comment("xxx) 属性，那么将只更新comment内容，其它内容保持不变
     *
     * @param mapper
     * @param tableSchema
     * @param columnDefinitions
     * @return
     */
    AlterColumn modify(SqlMapper mapper, String tableSchema, ColumnDefinition... columnDefinitions);

    /**
     *  部分修改字段信息
     *  比如： 只设置了 ColumnDefinition.comment("xxx) 属性，那么将只更新comment内容，其它内容保持不变
     *
     * @param mapper
     * @param tableSchema
     * @param columnDefinitions
     * @return
     */
    AlterColumn modify(SqlMapper mapper, String tableSchema, List<ColumnDefinition> columnDefinitions);

    /**
     * 完全修改字段信息
     *
     * @param columnDefinitions
     * @return
     */
    AlterColumn modifyAll(ColumnDefinition... columnDefinitions);

    /**
     * 完全修改字段信息
     *
     * @param columnDefinitions
     * @return
     */
    AlterColumn modifyAll(List<ColumnDefinition> columnDefinitions);

    AlterColumn drop(String... columnNames);

    AlterColumn drop(List<String> columnNames);

    void fire(SqlMapper mapper);
}

/**
 * SQL.pie(AlterColumn.class)
 *    .table("test")
 *    .add(ColumnDefinition.builder().a().b().build())
 *    .modify() // selective
 *    .modifyAll()
 *    .fire(sqlMapper);
 *
 *
 */
