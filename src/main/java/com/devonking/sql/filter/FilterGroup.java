package com.devonking.sql.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A group of filters and/or sub-groups
 *
 * @author Devon King
 * @since 1.0
 */
public class FilterGroup {
    private static final Logger log = LoggerFactory.getLogger(FilterGroup.class);

    /**
     * An id generator
     */
    private static final ThreadLocal<AtomicInteger> ID_GENERATOR = new ThreadLocal<>();

    /**
     * An unique number represents current object instance within the thread
     */
    private int id;

    /**
     * A magic box for putting filters and sub-groups
     */
    private List<Object> magicBox = new ArrayList<>();

    public FilterGroup() {
        synchronized (this) {
            if (null == ID_GENERATOR.get()) {
                ID_GENERATOR.set(new AtomicInteger());
            }
        }
        this.id = ID_GENERATOR.get().incrementAndGet();

        log.debug("{} - created", getName());
    }

    public String getName() {
        return "group-" + id + "@thread-" + Thread.currentThread().getId();
    }

    /**
     * Group filters with a link type of AND
     * <p>E.g., x = 1 and y = 2
     *
     * @param filters
     * @return
     */
    public FilterGroup and(Filter ... filters) {
        for(Filter filter : filters) {
            addToBox(Link.AND, filter);
        }

        return this;
    }

    /**
     * Group filters with a link type o OR
     * <p>E.g. x = 1 or y = 2
     *
     * @param filters
     * @return
     */
    public FilterGroup or(Filter ... filters) {
        for(Filter filter : filters) {
            addToBox(Link.OR, filter);
        }

        return this;
    }

    /**
     * Add sub-groups with AND link-type
     *
     * @param filterGroups
     * @return
     */
    public FilterGroup and(FilterGroup ... filterGroups) {

        for(FilterGroup filterGroup : filterGroups) {
            addToBox(Link.AND, filterGroup);
        }

        return this;
    }

    /**
     * Add sub-groups with OR link-type
     *
     * @param filterGroups
     * @return
     */
    public FilterGroup or(FilterGroup ... filterGroups) {

        for(FilterGroup filterGroup : filterGroups) {
            addToBox(Link.OR, filterGroup);
        }

        return this;
    }

    /**
     * Concatenate all filters in this group and its sub-groups into a Sql string
     *
     * @return
     */
    public String toSql() {
        StringBuilder sql = new StringBuilder();

        for(Object o : magicBox) {
            if(o instanceof FilterLink) {
                sql.append(((FilterLink) o).toSql()).append(" ");
            }

            if(o instanceof FilterGroupLink) {
                sql.append(((FilterGroupLink) o).toSql()).append(" ");
            }
        }

        if(sql.length() > 0) {
            sql.deleteCharAt(sql.length() - 1);

            if(magicBox.size() > 1) {
                sql.insert(0, "(").append(")");
            }
        }

        return sql.toString();
    }

    /**
     * Get prepared values
     *
     * @return
     */
    public Map<String, Object> getPreparedValues() {
        Map<String, Object> preparedValues = new HashMap<>(16);
        for(Object o : magicBox) {
            if(o instanceof FilterLink) {
                preparedValues.putAll(((FilterLink) o).getFilter().getPreparedValue());
            }

            if(o instanceof FilterGroupLink) {
                preparedValues.putAll(((FilterGroupLink) o).getFilterGroup().getPreparedValues());
            }
        }

        return preparedValues;
    }

    /**
     * Clear resource
     */
    public static void clear() {
        ID_GENERATOR.remove();
    }

    private void addToBox(Link link, Filter filter) {
        if(magicBox.size() == 0) {
            magicBox.add(new FilterLink(Link.EMPTY, filter));
        } else {
            magicBox.add(new FilterLink(link, filter));
        }
        log.debug("{} - added {}:{}", getName(), filter.getName(), link.name());
    }

    private void addToBox(Link link, FilterGroup filterGroup) {
        if(magicBox.size() == 0) {
            magicBox.add(new FilterGroupLink(Link.EMPTY, filterGroup));
        } else {
            magicBox.add(new FilterGroupLink(link, filterGroup));
        }
        log.debug("{} - added {}:{}", getName(), filterGroup.getName(), link.name());
    }
}
