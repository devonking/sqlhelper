package com.devonking.sql.pie.core;

import com.devonking.sql.constant.Constant;
import com.devonking.sql.util.StringUtils;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDefinition {
    @NonNull
    private String name;
    private String newName;
    private DataType type;
    private Integer length;
    private Integer precision;
    private Integer scale;
    private Boolean unsigned;
    private Boolean nullable;
    private String defaultValue;
    private String comment;

    public String toSql() {
        String typ = buildDataType();
        String nul = buildNullable();
        String def = buildDefaultValue();
        String com = buildComment();
        return StringUtils.joinWith(Constant.DELIMITER_SINGLE_SPACE, true, name, newName, typ, nul, def, com);
    }

    private String buildDataType() {
        if (Objects.isNull(type)) {
            return null;
        }

        StringBuilder dataType = new StringBuilder(type.name());
        int len = Objects.isNull(length) ? 0: length;
        int pre = Objects.isNull(precision) ? 0: precision;
        int sca = Objects.isNull(scale) ? 0: scale;
        switch (type) {
            case TINYINT:
            case INT:
            case BIGINT:
                dataType.append("(").append(precision).append(")");
                if (Objects.nonNull(unsigned) && unsigned) {
                    dataType.append(Constant.DELIMITER_SINGLE_SPACE).append(Constant.COLUMN_DEFINITION_PROPERTY_UNSIGNED);
                }
                break;
            case CHAR:
            case VARCHAR:
                dataType.append("(").append(len).append(")");
                break;
            case DECIMAL:
            case DOUBLE:
            case FLOAT:
                dataType.append("(").append(pre).append(", ").append(sca).append(")");
                break;
            default:
                // do nothing
                break;
        }

        return dataType.toString();
    }

    private String buildNullable() {
        if (Objects.isNull(nullable)) {
            return null;
        }

        return nullable? Constant.COLUMN_DEFINITION_PROPERTY_NULL : Constant.COLUMN_DEFINITION_PROPERTY_NOT_NULL;
    }

    private String buildDefaultValue() {
        if (Objects.isNull(defaultValue)) {
            return null;
        }

        return Constant.COLUMN_DEFINITION_PROPERTY_DEFAULT + Constant.DELIMITER_SINGLE_SPACE+ "'" + defaultValue + "'";
    }

    private String buildComment() {
        if (Objects.isNull(comment)) {
            return null;
        }

        return Constant.COLUMN_DEFINITION_PROPERTY_COMMENT + Constant.DELIMITER_SINGLE_SPACE + "'" + comment + "'";
    }

}
