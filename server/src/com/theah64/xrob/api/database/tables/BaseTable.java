package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.models.Delivery;
import org.json.JSONObject;

/**
 * Created by theapache64 on 11/22/2015.
 */
public class BaseTable<T> {

    public static final String COLUMN_ID = "id";
    protected static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    protected static final String COLUMN_CREATED_AT = "created_at";
    private static final String ERROR_MESSAGE_UNDEFINED_METHOD = "Undefined method.";

    public T get(final String column, final String value) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public boolean add(@Nullable final String userId, final JSONObject jsonObject) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public boolean add(T newInstance) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public void addv2(T newInstance) throws Exception {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public String get(final String byColumn, final String byValues, final String columnToReturn) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public static class Factory {
        public static BaseTable getTable(String deliveryType) {
            switch (deliveryType) {

                //Returning message table
                case Delivery.TYPE_MESSAGES:
                    return Messages.getInstance();

                default:
                    throw new IllegalArgumentException("No table defined for the type " + deliveryType);
            }
        }
    }
}
