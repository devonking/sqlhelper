package com.devonking.sql.filter;

/**
 * description in here
 *
 * @author Devon King
 * @since 1.0
 */
public class FilterGroupLink {

    private Link link;

    private FilterGroup filterGroup;

    public FilterGroupLink(Link link, FilterGroup filterGroup) {
        this.link = link;
        this.filterGroup = filterGroup;
    }

    public FilterGroup getFilterGroup() {
        return filterGroup;
    }

    public String toSql() {
        switch (link) {
            case AND:
                return "AND " + filterGroup.toSql();

            case OR:
                return "OR " + filterGroup.toSql();

            case EMPTY:
                return filterGroup.toSql();
        }

        return "";
    }
}
