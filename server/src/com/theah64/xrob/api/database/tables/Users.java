package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.database.utils.SelectQuery;
import com.theah64.xrob.api.database.utils.UpdateQuery;
import com.theah64.xrob.api.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Shifar Shifz on 11/22/2015.
 */
public class Users extends BaseTable<User> {

    public static final String COLUMN_IMEI = "imei";
    public static final String COLUMN_GCM_ID = "gcm_id";
    public static final java.lang.String COLUMN_API_KEY = "api_key";
    private static final String TABLE_USERS = "users";

    private Users() {
    }

    private static final Users instance = new Users();

    public static Users getInstance() {
        return instance;
    }

    @Override
    public User get(String byColumn, String byValue) {

        final String query = new SelectQuery()
                .select("*")
                .from(TABLE_USERS)
                .where(byColumn)
                .limit(1)
                .build();

        final java.sql.Connection connection = Connection.getConnection();
        User user = null;

        try {

            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, byValue);

            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //Collecting user
                final String apiKey = rs.getString(COLUMN_API_KEY);
                final String gcmId = rs.getString(COLUMN_GCM_ID);
                user = new User(null, null, apiKey, gcmId);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    /**
     * Used to add new user to the database
     */
    @Override
    public boolean add(User user) {

        final String addUserQuery = "INSERT INTO users (name,gcm_id,api_key,imei) VALUES (?,?,?,?);";
        final java.sql.Connection connection = Connection.getConnection();

        //To track the success
        boolean isUserAdded = false;

        try {
            final PreparedStatement ps = connection.prepareStatement(addUserQuery);
            ps.setString(1, user.getName());
            ps.setString(2, user.getGCMId());
            ps.setString(3, user.getApiKey());
            ps.setString(4, user.getIMEI());
            isUserAdded = ps.executeUpdate() == 1;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isUserAdded;
    }

    /**
     * Used to get a specific column by the {byColumn} and {byValue}
     */
    @Override
    public String get(String byColumn, String byValue, String columnToReturn) {

        final SelectQuery selectQuery = new SelectQuery();

        selectQuery
                .select(columnToReturn)
                .from(TABLE_USERS)
                .where(byColumn)
                .limit(1);

        String resultValue = null;
        final java.sql.Connection connection = Connection.getConnection();

        try {
            final PreparedStatement ps = connection.prepareStatement(selectQuery.build());
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
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultValue;
    }

    @Override
    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {

        final UpdateQuery updateQuery = new UpdateQuery();

        //Preparing query
        final String query = updateQuery
                .update(TABLE_USERS)
                .set(updateColumn)
                .where(whereColumn)
                .build();

        boolean isUserUpdated = false;
        final java.sql.Connection connection = Connection.getConnection();


        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newUpdateColumnValue);
            ps.setString(2, whereColumnValue);
            isUserUpdated = ps.executeUpdate() == 1;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isUserUpdated;
    }
}
