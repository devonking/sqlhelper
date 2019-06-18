package com.devonking.sql.filter;

import com.devonking.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * An utility for building the filtering SQL clause
 *
 * <p>
 *     For example:
 *     FilterHelper pie = new FilterHelper();
 *     pie
 *          .and("x", "1")
 *          .and("y", "2")
 *          .begin()
 *          .and("a", 3)
 *          .or("a", 4)
 *          .end();
 *
 *      System.out.println(pie.sql());
 *
 *
 *      It will return you:
 *
 *          x = '1' AND y = '2' AND (a = '3' OR a = '4')
 * </p>
 *
 * The .and() function or the .or() function which is right behind the .begin() function
 * determines how the current new group links to the previous group.
 *
 * For example:
 *  pie.and("x", 1).begin().and("y", 2).or("z", 3).end();
 *  ==>
 *      x = '1' AND (y = '2' OR z = '3')
 *
 *  pie.and("x", 1).begin().or("y", 2).or("z", 3).end();
 *  ==>
 *      x = '1' OR (y = '2' OR z = '3')
 *
 *
 * @author Devon King
 * @since 1.0
 */
public class FilterHelper {
    private static final Logger log = LoggerFactory.getLogger(FilterHelper.class);

    /**
     * The left bracket
     */
    private static final String LEFT_BRACKET = "(";
    /**
     * The right bracket
     */
    private static final String RIGHT_BRACKET = ")";

    /**
     * A group bucket (first in last out)
     */
    private ConcurrentLinkedDeque<FilterGroup> groupBucket;

    /**
     * The first group in the bucket
     */
    private FilterGroup rootGroup;

    /**
     * The current group which accepts all the operations
     */
    private FilterGroup currentGroup;

    /**
     * A flag indicates whether or not add the WHERE keyword to the front of the sql
     */
    private boolean where = false;

    /**
     * A flag indicates if need to start a new group
     */
    private boolean begin = false;

    private boolean preparedMode = false;

    public FilterHelper() {
        this(false);
    }

    public FilterHelper(boolean preparedMode) {
        this(preparedMode, true);
    }

    public FilterHelper(boolean preparedMode, boolean clearFilter) {
        if(clearFilter) {
            clearFilter();
        }

        this.preparedMode = preparedMode;
        groupBucket = new ConcurrentLinkedDeque<>();
        rootGroup = new FilterGroup();
        groupBucket.push(rootGroup);

        currentGroup = rootGroup;
    }

    /**
     * An and-operation that adds a filter to the current group
     *
     * @param key
     * @param value
     * @return
     */
    public FilterHelper and(String key, Object value) {
        if(value instanceof Operator) {
            return and(key, (Operator) value, null);
        } else {
            return and(key, Operator.EQ, value);
        }
    }

    /**
     * An and-operation that adds a filter to the current group
     *
     * @param key
     * @param operator
     * @param value
     * @return
     */
    public FilterHelper and(String key, Operator operator, Object value) {
        if(begin) {
            begin(Link.AND);
        }

        currentGroup.and(new Filter(key, operator, value, preparedMode));

        return this;
    }

    /**
     * An or-operation that adds a filter to the current group
     *
     * @param key
     * @param value
     * @return
     */
    public FilterHelper or(String key, Object value) {
        if(value instanceof Operator) {
            return or(key, (Operator) value, null);
        } else {
            return or(key, Operator.EQ, value);
        }
    }

    /**
     * An or-operation that adds a filter to the current group
     *
     * @param key
     * @param operator
     * @param value
     * @return
     */
    public FilterHelper or(String key, Operator operator, Object value) {

        if(begin) {
            begin(Link.OR);
        }

        currentGroup.or(new Filter(key, operator, value, preparedMode));

        return this;
    }

    /**
     * Mark to create a new group when the next operation comes
     *
     * @return
     */
    public FilterHelper begin() {
        begin = true;

        return this;
    }

    /**
     * Create a new group, push it to the bucket and then change the current group to it
     *
     * @param link
     */
    private void begin(Link link) {
        begin = false;

        FilterGroup next = new FilterGroup();
        groupBucket.push(next);

        switch (link) {
            case AND:
                currentGroup.and(next);
                break;
            case OR:
                currentGroup.or(next);
                break;
            default:
                throw new RuntimeException("Link type not support");
        }

        currentGroup = next;
    }

    /**
     * Pop out a group from bucket and change the current group to it
     *
     * @return
     */
    public FilterHelper end() {
        groupBucket.pop();
        currentGroup = groupBucket.peek();
        if(null == currentGroup) {
            throw new RuntimeException("Group bucket is empty");
        }
        return this;
    }

    /**
     * Get sql
     *
     * @return
     */
    public String sql() {
        return sql(true);
    }

    /**
     * Get sql
     *
     * @param trimBracket
     * @return
     */
    public String sql(boolean trimBracket) {
        String sql = rootGroup.toSql();
        if(ObjectUtil.blank(sql)) {
            log.debug("SQL is blank");
            return "";
        }

        if(trimBracket && sql.startsWith(LEFT_BRACKET) && sql.endsWith(RIGHT_BRACKET)) {
            sql = sql.substring(1, sql.length() - 1);
            log.debug("SQL trimmed");
        }

        if(where) {
            log.debug("SQL added with WHERE prefix");
            return "WHERE " + sql;
        } else {
            return sql;
        }
    }

    /**
     * Get prepared values
     *
     * @return
     */
    public Map<String, Object> values() {
        return rootGroup.getPreparedValues();
    }

    /**
     * Add a WHERE keyword to the front of the output sql
     *
     * @return
     */
    public FilterHelper where() {
        where = true;

        return this;
    }

    /**
     * Clear filter
     */
    public void clearFilter() {
        Filter.clear();

        FilterGroup.clear();
    }
}
