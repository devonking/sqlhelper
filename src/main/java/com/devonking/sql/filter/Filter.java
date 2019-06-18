package com.devonking.sql.filter;

import com.devonking.sql.util.ObjectUtil;
import com.devonking.sql.util.LangUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Sql filter
 *
 * <p>E.g.,
 *      x > y
 * </p>
 *
 * @author Devon King
 * @since 1.0
 */
public class Filter {
    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    /**
     * An id generator
     */
    private static final ThreadLocal<AtomicInteger> ID_GENERATOR = new ThreadLocal<>();

    /**
     * sub placeholder for value after BETWEEN
     */
    private static final String SUB_PLACEHOLDER_BETWEEN = "BT";

    /**
     * sub placeholder for value after AND
     */
    private static final String SUB_PLACEHOLDER_AND = "AD";

    /**
     * An identity of this object's instance within current thread
     */
    private int id;

    /**
     * The left operand of the expression
     */
    private String key;

    /**
     * The operator of the expression
     */
    private Operator operator;

    /**
     * The right operand of the expression
     */
    private Object value;

    /**
     * Prepared value
     */
    private Map<String, Object> preparedValue = new HashMap<>(3);

    /**
     * Prepared statement mode
     */
    private boolean preparedMode = false;

    private boolean variable = false;

    private Object betweenVar;
    private Object andVar;

    public Filter(String key, Object value) {
        this(key, Operator.EQ, value);
    }

    public Filter(String key, Operator operator, Object value) {
        this(key, operator, value, false);
    }

    public Filter(String key, Operator operator, Object value, boolean preparedMode) {
        this.key = key;
        this.operator = operator;
        this.value = value;
        this.preparedMode = preparedMode;

        synchronized (this) {
            if (null == ID_GENERATOR.get()) {
                ID_GENERATOR.set(new AtomicInteger());
            }
        }
        this.id = ID_GENERATOR.get().incrementAndGet();

        if(log.isDebugEnabled()) {
            if (null == value) {
                log.debug("{} - created: {} {}", getName(), key, operator);
            } else {
                log.debug("{} - created: {} {} {}", getName(), key, operator, value);
            }
        }

        this.variable = isVariable(value);

        prepareValue(operator, value);
    }

    /**
     * Get filter name
     *
     * @return
     */
    public String getName() {
        return "filter-" + id + "@thread-" + Thread.currentThread().getId() ;
    }

    /**
     * Set prepared mode
     *
     * @param mode true it is prepared mode otherwise not
     */
    public void setPreparedMode(boolean mode) {
        this.preparedMode = mode;
    }


    /**
     * Get prepared value
     *
     * @return
     */
    public Map<String, Object> getPreparedValue() {
        if(preparedMode) {
            return preparedValue;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    /**
     * To string Sql expression
     *
     * @return
     */
    public String toSql() {
        stopSQLInjection(key, value);

        switch (operator) {
            case EQ:
                return concatSimpleKeyValue(" = ", preparedMode);
            case NEQ:
                return concatSimpleKeyValue(" != ", preparedMode);
            case GT:
                return concatSimpleKeyValue(" > ", preparedMode);
            case GT_EQ:
                return concatSimpleKeyValue(" >= ", preparedMode);
            case LT:
                return concatSimpleKeyValue(" < ", preparedMode);
            case LT_EQ:
                return concatSimpleKeyValue(" <= ", preparedMode);
            case LIKE:
                return concatLikeKeyValue(" LIKE ", true, true, preparedMode);
            case NOT_LIKE:
                return concatLikeKeyValue(" NOT LIKE ", true, true, preparedMode);
            case LF_LIKE:
                return concatLikeKeyValue(" LIKE ", true, false, preparedMode);
            case NOT_LF_LIKE:
                return concatLikeKeyValue(" NOT LIKE ", true, false, preparedMode);
            case RT_LIKE:
                return concatLikeKeyValue(" LIKE ", false, true, preparedMode);
            case NOT_RT_LIKE:
                return concatLikeKeyValue(" NOT LIKE ", false, true, preparedMode);
            case BETWEEN_AND:
                return concatBetweenAndKeyValue(preparedMode);
            case NULL:
                return concatNullKeyValue(" IS NULL");
            case NOT_NULL:
                return concatNullKeyValue(" IS NOT NULL");
            case IN:
                return concatInKeyValue(" IN ", preparedMode);
            case NOT_IN:
                return concatInKeyValue(" NOT IN ", preparedMode);
            default:
                throw new RuntimeException("#SQL Error - Operator not supported");
        }
    }

    /**
     * Clear resource
     */
    public static void clear() {
        ID_GENERATOR.remove();
    }

    /**
     * Get filter value
     *
     * @return
     */
    private Object getValue() {
        return getValue("");
    }

    /**
     * Get filter value
     *
     * @param subPlaceholder
     * @return
     */
    private Object getValue(String subPlaceholder) {
        return preparedValue.get(getPlaceholder(subPlaceholder));
    }

    /**
     * Get placeholder
     *
     * @return
     */
    private String getPlaceholder() {
        return getPlaceholder("");
    }

    /**
     * Get placeholder with sub placeholder
     *
     * @param subPlaceholder
     * @return
     */
    private String getPlaceholder(String subPlaceholder) {
        return "w" + subPlaceholder + this.id;
    }

    private boolean isVariable(Object val) {
        return val instanceof String && LangUtils.isVar((String) val);
    }

    /**
     * Preparing value
     *
     * @param operator
     * @param value
     */
    private void prepareValue(Operator operator, Object value) {
        switch (operator) {
            case NULL:
            case NOT_NULL:
                break;
            case BETWEEN_AND:
                if (!(value instanceof List)) {
                    throw new RuntimeException("#SQL Error - BETWEEN x AND y: java.lang.List type of value was expected");
                }

                if(!isVariable(((List) value).get(0))) {
                    preparedValue.put(getPlaceholder(SUB_PLACEHOLDER_BETWEEN), ((List) value).get(0));
                } else {
                    betweenVar = LangUtils.var((String) ((List) value).get(0));
                }
                if(!isVariable(((List) value).get(1))) {
                    preparedValue.put(getPlaceholder(SUB_PLACEHOLDER_AND), ((List) value).get(1));
                } else {
                    andVar = LangUtils.var((String) ((List) value).get(1));
                }
                break;
            case IN:
            case NOT_IN:
                if(!variable) {
                    List items = parseInValues();
                    preparedValue.put(getPlaceholder(), items);
                }
                break;
            default:
                if(!variable) {
                    preparedValue.put(getPlaceholder(), value);
                }
        }
    }

    /**
     * Concat simple key-value
     *
     * @param operator
     * @param preparedStatement
     * @return
     */
    private String concatSimpleKeyValue(String operator, boolean preparedStatement) {
        StringBuilder filterSql = new StringBuilder();
        filterSql
                .append(key)
                .append(operator);
        if(variable) {
            filterSql
                    .append(LangUtils.var((String) value));
        } else {
            // Prepared statement mode
            if (preparedStatement) {
                filterSql
                        .append("#{")
                        .append(getPlaceholder())
                        .append("}");
            } else {
                filterSql
                        .append("'")
                        .append(getValue())
                        .append("'");
            }
        }

        return filterSql.toString();
    }

    /**
     * Concat key-value for like operator
     *
     * @param operator
     * @param left
     * @param right
     * @param preparedMode
     * @return
     */
    private String concatLikeKeyValue(String operator, boolean left, boolean right, boolean preparedMode) {
        StringBuilder filterSql = new StringBuilder();
        filterSql
                .append(key)
                .append(operator);
        
        // Prepared statement mode
        if(preparedMode) {
            filterSql.append("CONCAT(");
            if(left) {
                filterSql.append("'%', ");
            }
            filterSql
                    .append("#{")
                    .append(getPlaceholder())
                    .append("}");
            if(right) {
                filterSql.append(", '%'");
            }
            filterSql.append(")");
        } else {
            filterSql.append("'");
            if(left) {
                filterSql.append("%");
            }
            filterSql.append(getValue());
            if(right) {
                filterSql.append("%");
            }
            filterSql.append("'");
        }

        return filterSql.toString();
    }

    /**
     * Concat key-value for between-and operator
     *
     * @param preparedMode
     * @return
     */
    private String concatBetweenAndKeyValue(boolean preparedMode) {
        StringBuilder filterSql = new StringBuilder();
        if(preparedMode) {
            filterSql.append(key)
                    .append(" BETWEEN #{")
                    .append(getPlaceholder())
                    .append("} AND #{")
                    .append(getPlaceholder())
                    .append("}");
        } else {
            filterSql.append(key)
                    .append(" BETWEEN ");
            if(ObjectUtil.isNull(betweenVar)) {
                filterSql.append("'").append(getValue(SUB_PLACEHOLDER_BETWEEN)).append("'");
            } else {
                filterSql.append(betweenVar);
            }
            filterSql.append(" AND ");
            if(ObjectUtil.isNull(andVar)) {
                filterSql.append("'").append(getValue(SUB_PLACEHOLDER_AND)).append("'");
            } else {
                filterSql.append(andVar);
            }
        }

        return filterSql.toString();
    }

    /**
     * Concat key-value for null operator
     *
     * @param operator
     * @return
     */
    private String concatNullKeyValue(String operator) {
        return new StringBuilder()
                .append(key)
                .append(operator)
                .toString();
    }

    /**
     * Parse value for in operator
     *
     * @return
     */
    private List parseInValues() {
        List items;
        if(value instanceof List) {
            items = (List) value;
        } else if (value instanceof String) {
            items = splitValues((String) value);
        } else {
            items = Arrays.asList(value);
        }

        return items;
    }

    /**
     * Concat key-value for in operator
     *
     * @param operator
     * @param preparedMode
     * @return
     */
    private String concatInKeyValue(String operator, boolean preparedMode) {
        StringBuilder filterSql = new StringBuilder();
        filterSql
                .append(key)
                .append(operator);

        List items = (List) getValue();

        if(preparedMode) {
            filterSql.append("<foreach collection=\"").append(getPlaceholder()).append("\" item=\"itm\" separator=\",\" open=\"(\" close=\")\">#{itm}</foreach>");
        } else {
            filterSql.append(combineValues(items));
        }

        return filterSql.toString();
    }

    /**
     * Split values
     *
     * @param values
     * @return
     */
    private List<String> splitValues(String values) {
        return Arrays.asList(values.split(","));
    }

    /**
     * Combining values into a string
     *
     * @param values
     * @return
     */
    private String combineValues(List values) {
        StringBuilder combinedValues = new StringBuilder();

        values.forEach(v -> combinedValues.append("'").append(v).append("', "));

        if(combinedValues.length() > 0) {
            combinedValues.delete(combinedValues.length() - 2, combinedValues.length());
        }

        return "(" + combinedValues.toString() + ")";
    }

    /**
     * Dangerous keywords may cause SQL injection attack
     */
    private static final String[] DANGEROUS_WORDS = {"(", ")", "*", "%", ";"};

    /**
     * Check Sql injection
     *
     * @param key
     * @param value
     */
    private void stopSQLInjection(String key, Object value) {
        boolean dangerousFound = false;

        for(String word : DANGEROUS_WORDS) {
            if(ObjectUtil.notBlank(key)) {
                if(-1 != key.toUpperCase().indexOf(word)) {
                    dangerousFound = true;
                    break;
                }
            }

            if(ObjectUtil.notNull(value)) {
                if(findPieceInTheCake(word, value)) {
                    dangerousFound = true;
                    break;
                }

                // Check dangerous collection type of value
                if(value instanceof List && ObjectUtil.notEmpty((List) value)) {
                    List values = (List) value;
                    for(Object v : values) {
                        if(findPieceInTheCake(word, v)) {
                            dangerousFound = true;
                            break;
                        }
                    }
                }
            }
        }

        if(dangerousFound) {
            throw new RuntimeException("#Filter - Caution! Caution! Caution! something bad and dangerous found in SQL: " + key + " " + operator + " " + value);
        }
    }

    /**
     * Can you find the piece in the cake?
     *
     * @param piece
     * @param cake
     * @return
     */
    private boolean findPieceInTheCake(String piece, Object cake) {

        return cake instanceof String
                && ObjectUtil.notBlank((String) cake)
                && -1 != ((String) cake).toUpperCase().indexOf(piece);
    }
}
