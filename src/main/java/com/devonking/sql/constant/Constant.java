package com.devonking.sql.constant;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public interface Constant {

    /**
     * 保留关键字 SQL KEY
     */
    String RESERVED_KEYWORD_SQL = "__SQL";
    /**
     * 保留关键字 自增主键ID
     */
    String RESERVED_KEYWORD_AUTO_ID = "__AUTO_ID";

    /**
     * 逗号分隔符
     */
    String DELIMITER_COMMA = ", ";
    /**
     * 空格分隔符
     */
    String DELIMITER_SINGLE_SPACE = " ";

    /**
     * SELECT SQL 关键字
     */
    String KEYWORD_SELECT = "SELECT";
    /**
     * INSERT SQL 关键字
     */
    String KEYWORD_INSERT = "INSERT";
    /**
     * UPDATE SQL 关键字
     */
    String KEYWORD_UPDATE = "UPDATE";
    /**
     * DELETE SQL 关键字
     */
    String KEYWORD_DELETE = "DELETE";

    /**
     * 字段定义属性名称
     */
    String COLUMN_DEFINITION_PROPERTY_UNSIGNED = "UNSIGNED";
    String COLUMN_DEFINITION_PROPERTY_NULL = "NULL";
    String COLUMN_DEFINITION_PROPERTY_NOT_NULL = "NOT NULL";
    String COLUMN_DEFINITION_PROPERTY_COMMENT = "COMMENT";
    String COLUMN_DEFINITION_PROPERTY_DEFAULT = "DEFAULT";

    /**
     * 字段信息表字段名称
     */
    String SCHEMA_INFO_COLUMNS_NAME = "column_name";
    String SCHEMA_INFO_COLUMNS_DEFAULT = "column_default";
    String SCHEMA_INFO_COLUMNS_NULLABLE = "is_nullable";
    String SCHEMA_INFO_COLUMNS_COMMENT = "column_comment";
    String SCHEMA_INFO_COLUMNS_LENGTH = "character_maximum_length";
    String SCHEMA_INFO_COLUMNS_PRECISION = "numeric_precision";
    String SCHEMA_INFO_COLUMNS_SCALE = "numeric_scale";
    String SCHEMA_INFO_COLUMNS_DATA_TYPE = "data_type";
    String SCHEMA_INFO_COLUMNS_COLUMN_TYPE = "column_type";
    String SCHEMA_INFO_COLUMNS_TABLE_SCHEMA = "table_schema";
    String SCHEMA_INFO_COLUMNS_TABLE_NAME = "table_name";
    /**
     * 字段信息表名称
     */
    String SCHEMA_INFO_COLUMNS_TABLE = "information_schema.columns";
}
