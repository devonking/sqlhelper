package com.devonking.sql.pie.simple;

import com.devonking.sql.util.ObjectUtil;
import com.devonking.sql.constant.Constant;
import com.devonking.sql.pie.SQL;
import com.devonking.sql.pie.core.*;
import com.devonking.sql.pie.statement.AlterColumn;
import com.devonking.sql.pie.statement.Select;
import com.devonking.sql.mybatis.SqlMapper;
import com.devonking.sql.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Devon King
 * @since 1.0
 */
public class SimpleAlterColumn implements AlterColumn {

    private Table table;

    // 待新增字段列表
    private List<ColumnDefinition> addColumnDefinitions = new ArrayList<>();

    // 待修改字段列表
    private List<ColumnDefinition> modifyColumnDefinitions = new ArrayList<>();

    // 待删除字段列表
    private List<String> dropColumns = new ArrayList<>();

    // 当前字段信息
    private Map<String, Map<String, Object>> realColumnsDefinition;

    @Override
    public AlterColumn add(ColumnDefinition... columnDefinitions) {
        addColumnDefinitions.addAll(Arrays.asList(columnDefinitions));
        return this;
    }

    @Override
    public AlterColumn add(List<ColumnDefinition> columnDefinitions) {
        addColumnDefinitions.addAll(columnDefinitions);
        return this;
    }

    @Override
    public AlterColumn modify(SqlMapper mapper, String tableSchema, ColumnDefinition... columnDefinitions) {
        return modify(mapper, tableSchema, Arrays.asList(columnDefinitions));
    }

    @Override
    public AlterColumn modify(SqlMapper mapper, String tableSchema, List<ColumnDefinition> columnDefinitions) {
        if (null == realColumnsDefinition) {
            realColumnsDefinition = getRealColumnsDefinition(mapper, tableSchema, table.getName());
        }

        columnDefinitions.forEach(cd -> {
            Map<String, Object> rd = realColumnsDefinition.get(cd.getName());
            if (null != rd) {
                modifyColumnDefinitions.add(complementColumnDefinition(cd, rd));
            } else {
                throw new RuntimeException(String.format("#SQL.Pie#AlterColumn column '%s' not exists", cd.getName()));
            }
        });

        return this;
    }

    @Override
    public AlterColumn modifyAll(ColumnDefinition... columnDefinitions) {
        modifyColumnDefinitions.addAll(Arrays.asList(columnDefinitions));
        return this;
    }

    @Override
    public AlterColumn modifyAll(List<ColumnDefinition> columnDefinitions) {
        modifyColumnDefinitions.addAll(columnDefinitions);
        return this;
    }

    @Override
    public AlterColumn drop(String... columnNames) {
        return drop(Arrays.asList(columnNames));
    }

    @Override
    public AlterColumn drop(List<String> columnNames) {
        dropColumns.addAll(columnNames);
        return this;
    }

    @Override
    public void fire(SqlMapper mapper) {
        mapper.struct(shout());
    }

    @Override
    public String shout() {

        return "ALTER TABLE" +
                Constant.DELIMITER_SINGLE_SPACE +
                buildTable() +
                Constant.DELIMITER_SINGLE_SPACE +
                buildAction();
    }

    @Override
    public AlterColumn table(Table table) {
        this.table = table;
        return this;
    }

    @Override
    public AlterColumn table(String name) {
        return table(name, null);
    }

    @Override
    public AlterColumn table(String name, String alias) {
        return table(new Table(name, alias));
    }

    private String buildTable() {
        return table.getName();
    }

    private String buildAction() {
        StringBuilder action = new StringBuilder();

        if (ObjectUtil.notEmpty(addColumnDefinitions)) {
            addColumnDefinitions.forEach(ac -> action
                    .append("ADD COLUMN")
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append(ac.toSql())
                    .append(Constant.DELIMITER_COMMA));
        }

        if (ObjectUtil.notEmpty(modifyColumnDefinitions)) {
            modifyColumnDefinitions.forEach(mc -> {
                if (null == mc.getType()) {
                    throw new RuntimeException("#SQL.Pie#AlterColumn type is empty");
                }

                if (StringUtils.isNotBlank(mc.getNewName())) {
                    action
                            .append("CHANGE COLUMN")
                            .append(Constant.DELIMITER_SINGLE_SPACE)
                            .append(mc.toSql())
                            .append(Constant.DELIMITER_COMMA);
                } else {
                    action
                            .append("MODIFY COLUMN")
                            .append(Constant.DELIMITER_SINGLE_SPACE)
                            .append(mc.toSql())
                            .append(Constant.DELIMITER_COMMA);
                }
            });
        }

        if (ObjectUtil.notEmpty(dropColumns)) {
            dropColumns.forEach(dc -> action
                    .append("DROP COLUMN")
                    .append(Constant.DELIMITER_SINGLE_SPACE)
                    .append(dc)
                    .append(Constant.DELIMITER_COMMA));
        }

        StringUtils.trimTail(action, Constant.DELIMITER_COMMA);
        return action.toString();
    }

    /**
     * 获取字段当前的定义
     *
     * @param mapper
     * @param tableSchema
     * @param tableName
     * @return
     */
    private Map<String, Map<String, Object>> getRealColumnsDefinition(SqlMapper mapper, String tableSchema, String tableName) {
        if (StringUtils.isBlank(tableSchema)) {
            throw new RuntimeException("#SQL.Pie#AlterColumn table schema is empty");
        }

        if (StringUtils.isBlank(tableName)) {
            throw new RuntimeException("#SQL.Pie#AlterColumn table name is empty");
        }

        // 查询表字段定义
        List<Map<String, Object>> realColumns = SQL.pie(Select.class)
                .from(Constant.SCHEMA_INFO_COLUMNS_TABLE)
                .query(Columns.pie().get(
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_NAME).build(),
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_DEFAULT).build(),
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_NULLABLE).build(),
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_COMMENT).build(),
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_LENGTH).build(),
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_PRECISION).build(),
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_SCALE).build(),
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_DATA_TYPE).build(),
                        ColumnAliasPair.builder().column(Constant.SCHEMA_INFO_COLUMNS_COLUMN_TYPE).build()))
                .filter(Conditions.pie()
                        .andEq(Constant.SCHEMA_INFO_COLUMNS_TABLE_SCHEMA, tableSchema)
                        .andEq(Constant.SCHEMA_INFO_COLUMNS_TABLE_NAME, tableName))
                .fire(mapper);

        if (null == realColumns) {
            return new HashMap<>();
        }

        return realColumns.stream().collect(Collectors.toMap(m -> (String) m.get(Constant.SCHEMA_INFO_COLUMNS_NAME), Function.identity()));
    }

    /**
     * 使用字段当前的属性值补全未设值的字段属性
     *
     * @param columnDefinition
     * @param currentDefinition
     * @return
     */
    private ColumnDefinition complementColumnDefinition(ColumnDefinition columnDefinition, Map<String, Object> currentDefinition) {
        if (null == columnDefinition.getComment()) {
            columnDefinition.setComment((String) currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_COMMENT));
        }
        if (null == columnDefinition.getDefaultValue()) {
            columnDefinition.setDefaultValue((String) currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_DEFAULT));
        }
        if (null == columnDefinition.getNullable()) {
            columnDefinition.setNullable(Objects.equals("YES", currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_NULLABLE)));
        }

        DataType dataType = columnDefinition.getType();
        if (null == dataType) {
            String dt = (String) currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_DATA_TYPE);
            if (null != dt) {
                dataType = DataType.valueOf(dt.toUpperCase());
                columnDefinition.setType(dataType);
            }
        }

        if (null == dataType) {
            throw new RuntimeException("#SQL.Pie#AlterColumn data type cannot be determined");
        }

        switch (dataType) {
            case TINYINT:
            case INT:
            case BIGINT:
                if (null == columnDefinition.getPrecision()) {
                    columnDefinition.setPrecision(Integer.valueOf(currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_PRECISION) + ""));
                }
                if (null == columnDefinition.getUnsigned()) {
                    String columnType = (String) currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_COLUMN_TYPE);
                    if (null != columnType) {
                        columnDefinition.setUnsigned(columnType.toUpperCase().contains(Constant.COLUMN_DEFINITION_PROPERTY_UNSIGNED));
                    }
                }
                break;
            case CHAR:
            case VARCHAR:
                if (null == columnDefinition.getLength()) {
                    columnDefinition.setLength(Integer.valueOf(currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_LENGTH) + ""));
                }
                break;
            case DECIMAL:
            case DOUBLE:
            case FLOAT:
                if (null == columnDefinition.getPrecision()) {
                    columnDefinition.setPrecision(Integer.valueOf(currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_PRECISION) + ""));
                }
                if (null == columnDefinition.getScale()) {
                    columnDefinition.setScale(Integer.valueOf(currentDefinition.get(Constant.SCHEMA_INFO_COLUMNS_SCALE) + ""));
                }
                break;
            default:
                // do nothing
                break;
        }

        return columnDefinition;
    }
}