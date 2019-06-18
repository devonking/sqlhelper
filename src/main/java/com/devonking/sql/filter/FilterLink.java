package com.devonking.sql.filter;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class FilterLink {

    private Link link;

    private Filter filter;

    public FilterLink(Link link, Filter filter) {
        this.link = link;
        this.filter = filter;
    }

    public Filter getFilter() {
        return filter;
    }

    public String toSql() {
        switch (link) {
            case AND:
                return "AND " + filter.toSql();

            case OR:
                return "OR " + filter.toSql();

            case EMPTY:
                return filter.toSql();
        }

        return "";
    }
}
