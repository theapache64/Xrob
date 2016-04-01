package com.theah64.xrob.api.database.utils;

import com.theah64.xrob.api.utils.CommonUtils;

import java.util.*;

/**
 * Created by shifar on 31/12/15.
 */
public class InsertQuery {

    private static final String PREPARED = "?";
    private final StringBuilder queryBuilder;
    private final Map<String, String> values;

    public InsertQuery() {
        this.queryBuilder = new StringBuilder();
        this.values = new HashMap<>();
    }

    public InsertQuery insertInto(final String tableName) {
        queryBuilder.append("INSERT INTO ").append(tableName);
        return this;
    }

    public InsertQuery set(final String columnName, final String value) {
        values.put(columnName, value);
        return this;
    }

    public InsertQuery set(final String columnName, final boolean value) {
        //true  = 1 ,false = 0
        values.put(columnName, value ? "1" : "0");
        return this;
    }

    public String build() {

        //Adding column prefix
        queryBuilder.append(" (");

        //Adding columnNames
        for (final String columnName : this.values.keySet()) {
            queryBuilder.append(columnName).append(",");
        }

        //Removing last comma
        queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());
        //Closing column brace
        queryBuilder.append(") VALUES (");

        //Adding values
        for (final Map.Entry<String, String> entry : this.values.entrySet()) {

            String value = entry.getValue();

            if (value.equals(PREPARED) || CommonUtils.isNumeric(value)) {
                //Prepares statement value
                queryBuilder.append(value).append(",");
            } else {
                //Normal value
                queryBuilder.append("'").append(value).append("'").append(",");
            }

        }

        //Removing last comma
        queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());

        //Ending query
        return queryBuilder.append(");").toString();
    }
}
