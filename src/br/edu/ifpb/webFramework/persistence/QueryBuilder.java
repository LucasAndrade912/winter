package br.edu.ifpb.webFramework.persistence;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {
    private String tableName;
    private List<String> selectColumns = new ArrayList<>();
    private List<String> conditions = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private List<String> groupByColumns = new ArrayList<>();
    private List<String> orderByColumns = new ArrayList<>();
    private List<String> havingConditions = new ArrayList<>();
    private boolean distinct = false;
    private String orderByDirection = "ASC";

    public QueryBuilder(String tableName) {
        this.tableName = tableName;
    }

    // SELECT clause
    public QueryBuilder select(String... columns) {
        for (String column : columns) {
            selectColumns.add(column);
        }
        return this;
    }

    // DISTINCT clause
    public QueryBuilder distinct() {
        this.distinct = true;
        return this;
    }

    // WHERE clause
    public QueryBuilder where(String condition) {
        conditions.add(condition);
        return this;
    }

    public QueryBuilder and(String condition) {
        conditions.add("AND " + condition);
        return this;
    }

    public QueryBuilder or(String condition) {
        conditions.add("OR " + condition);
        return this;
    }

    public QueryBuilder not(String condition) {
        conditions.add("NOT " + condition);
        return this;
    }

    // JOIN clauses
    public QueryBuilder join(String table, String onCondition) {
        joins.add("JOIN " + table + " ON " + onCondition);
        return this;
    }

    public QueryBuilder leftJoin(String table, String onCondition) {
        joins.add("LEFT JOIN " + table + " ON " + onCondition);
        return this;
    }

    // GROUP BY clause
    public QueryBuilder groupBy(String... columns) {
        for (String column : columns) {
            groupByColumns.add(column);
        }
        return this;
    }

    // HAVING clause
    public QueryBuilder having(String condition) {
        havingConditions.add(condition);
        return this;
    }

    // ORDER BY clause
    public QueryBuilder orderBy(String... columns) {
        for (String column : columns) {
            orderByColumns.add(column);
        }
        return this;
    }

    public QueryBuilder orderByDirection(String direction) {
        if (direction.equalsIgnoreCase("ASC") || direction.equalsIgnoreCase("DESC")) {
            this.orderByDirection = direction;
        } else {
            throw new IllegalArgumentException("Order direction must be 'ASC' or 'DESC'");
        }
        return this;
    }

    // Build query
    public String build() {
        StringBuilder query = new StringBuilder();

        // SELECT clause
        query.append("SELECT ");
        if (distinct) query.append("DISTINCT ");
        query.append(selectColumns.isEmpty() ? "*" : String.join(", ", selectColumns));
        query.append(" FROM ").append(tableName);

        // JOINs
        if (!joins.isEmpty()) {
            query.append(" ").append(String.join(" ", joins));
        }

        // WHERE clause
        if (!conditions.isEmpty()) {
            query.append(" WHERE ").append(String.join(" ", conditions));
        }

        // GROUP BY clause
        if (!groupByColumns.isEmpty()) {
            query.append(" GROUP BY ").append(String.join(", ", groupByColumns));
        }

        // HAVING clause
        if (!havingConditions.isEmpty()) {
            query.append(" HAVING ").append(String.join(" AND ", havingConditions));
        }

        // ORDER BY clause
        if (!orderByColumns.isEmpty()) {
            query.append(" ORDER BY ").append(String.join(", ", orderByColumns))
                    .append(" ").append(orderByDirection);
        }

        return query.toString();
    }
}
