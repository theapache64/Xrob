package com.theah64.xrob.api.database.utils;

/**
 * Created by Shifar Shifz on 11/26/2015.
 */
public final class UpdateQuery {
    private final StringBuilder queryBuilder;

    public UpdateQuery() {
        this.queryBuilder = new StringBuilder();
    }

    public UpdateQuery update(String tableName) {
        this.queryBuilder.append("UPDATE ").append(tableName);
        return this;
    }

    public UpdateQuery set(String updateColumn) {
        this.queryBuilder.append(" SET ").append(updateColumn).append(" = ?");
        return this;
    }

    public UpdateQuery where(String whereColumn) {
        this.queryBuilder.append(" WHERE ").append(whereColumn).append(" = ?");
        return this;
    }

    public String build() {
        return this.queryBuilder.toString();
    }
}
