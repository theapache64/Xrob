package com.theah64.xrob.api.database.tables;

/**
 * Created by Shifar Shifz on 11/22/2015.
 */
public abstract class BaseTable<T> {

    public static final String COLUMN_ID = "id";
    protected static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    protected static final String COLUMN_CREATED_AT = "created_at";

    public abstract T get(final String column, final String value);

    public abstract boolean add(T newInstance);

    public abstract String get(final String byColumn, final String byValues, final String columnToReturn);

    public abstract boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue);
}
