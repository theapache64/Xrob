package com.theah64.xrob.api.database.utils;

/**
 * Created by Shifar Shifz on 11/22/2015.
 */
public class SelectQuery {

    private final StringBuilder queryBuilder;

    public SelectQuery() {
        queryBuilder = new StringBuilder();
    }

    public SelectQuery select(String columnName) {
        queryBuilder.append("SELECT ").append(columnName);
        return this;
    }

    public SelectQuery select(String[] columnNames) {
        queryBuilder.append("SELECT ");
        for (int i = 0; i < columnNames.length; i++) {
            queryBuilder.append(columnNames[i]);
            if (i < columnNames.length) {
                queryBuilder.append(",");
            }
        }
        return this;
    }


    public SelectQuery from(String tableName) {
        queryBuilder.append(" FROM ").append(tableName);
        return this;
    }

    public SelectQuery where(String byColumn, String byValue) {
        queryBuilder.append(" WHERE ").append(byColumn).append("='").append(byValue).append("'");
        return this;
    }

    public SelectQuery limit(int limit) {
        queryBuilder.append(" LIMIT ").append(limit);
        return this;
    }

    public String build() {
        return queryBuilder.toString();
    }

    public SelectQuery where(String byColumn) {
        queryBuilder.append(" WHERE ").append(byColumn).append(" = ?");
        return this;
    }
}
