package com.theah64.xrob.api.utils;

/**
 * Created by theapache64 on 11/9/16.
 */
public class PreparedUpdateQueryBuilder {

    private final StringBuilder queryBuilder = new StringBuilder();

    public PreparedUpdateQueryBuilder(final String tableName) {
        queryBuilder.append("UPDATE ").append(tableName).append(" SET ");
    }

    public PreparedUpdateQueryBuilder setIfNotNull(final String key, final String value, final boolean hasNext) {

        if (value != null) {

            queryBuilder.append(key).append(" = ").append(" '' ");

            if (hasNext) {
                queryBuilder.append(" , ");
            }
        }

        return this;
    }

    public PreparedUpdateQueryBuilder setIfNotNull(final String value, final String key1, final String value1, final boolean hasNext) {
        if (value != null) {
            queryBuilder.append(key1).append(" = ").append(value1);

            if (hasNext) {
                queryBuilder.append(" , ");
            }
        }

        return this;
    }

    public PreparedUpdateQueryBuilder setIfNotNull(final String value, final String key1, final String value1) {
        return setIfNotNull(value, key1, value1, true);
    }

    public PreparedUpdateQueryBuilder setIfNotNull(final String key, final String value) {
        return setIfNotNull(key, value, true);
    }


    public PreparedUpdateQueryBuilder set(final String key, final String value, final boolean hasNext) {

        queryBuilder.append(key).append("=").append(value);

        if (hasNext) {
            queryBuilder.append(" , ");
        }

        return this;
    }

    public PreparedUpdateQueryBuilder set(final String key, final String value) {
        return set(key, value, true);
    }


    public PreparedUpdateQueryBuilder setWhereClause(final String whereClause) {
        queryBuilder.append(" WHERE ").append(whereClause);
        return this;
    }

    public String build() {
        return queryBuilder.toString();
    }

}
