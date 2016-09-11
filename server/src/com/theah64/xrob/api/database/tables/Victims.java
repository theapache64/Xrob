package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Victim;
import com.theah64.xrob.api.utils.PreparedUpdateQueryBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 11/22/2015.
 */
public class Victims extends BaseTable<Victim> {

    public static final String COLUMN_IMEI = "imei";
    public static final String COLUMN_FCM_ID = "fcm_id";
    public static final java.lang.String COLUMN_API_KEY = "api_key";
    private static final String TABLE_VICTIMS = "victims";
    public static final String COLUMN_CLIENT_ID = "client_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_FCM_UPDATED_AT = "fcm_updated_at";

    private Victims() {
    }

    private static final Victims instance = new Victims();

    public static Victims getInstance() {
        return instance;
    }

    @Override
    public Victim get(String byColumn, String byValue) {

        final String query = String.format("SELECT id,name,email,phone,api_key,fcm_id FROM victims WHERE %s = ? LIMIT 1", byColumn);

        final java.sql.Connection connection = Connection.getConnection();
        Victim victim = null;

        try {

            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, byValue);

            final ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //Collecting victim
                final String id = rs.getString(COLUMN_ID);
                final String name = rs.getString(COLUMN_NAME);
                final String phone = rs.getString(COLUMN_PHONE);
                final String email = rs.getString(COLUMN_EMAIL);
                final String apiKey = rs.getString(COLUMN_API_KEY);
                final String fcmId = rs.getString(COLUMN_FCM_ID);
                victim = new Victim(name, phone, email, null, apiKey, fcmId, id);
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

        return victim;
    }

    /**
     * Used to add new victim to the database
     */
    @Override
    public boolean add(Victim victim) {

        final String addVictimQuery = "INSERT INTO victims (name,fcm_id,api_key,imei,phone,email) VALUES (?,?,?,?,?,?);";
        final java.sql.Connection connection = Connection.getConnection();

        //To track the success
        boolean isVictimAdded = false;

        try {
            final PreparedStatement ps = connection.prepareStatement(addVictimQuery);

            ps.setString(1, victim.getName());
            ps.setString(2, victim.getFCMId());
            ps.setString(3, victim.getApiKey());
            ps.setString(4, victim.getIMEI());
            ps.setString(5, victim.getPhone());
            ps.setString(6, victim.getEmail());

            isVictimAdded = ps.executeUpdate() == 1;

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

        return isVictimAdded;
    }

    @Override
    public void addv2(Victim victim) throws RuntimeException {
        if (!add(victim)) {
            throw new RuntimeException("Unexpected error while adding new victim");
        }
    }

    /**
     * Used to get a specific column by the {byColumn} and {byValue}
     */
    @Override
    public String get(String byColumn, String byValue, String columnToReturn) {


        final String query = String.format("SELECT %s FROM victims WHERE %s = ?", columnToReturn, byColumn);

        String resultValue = null;
        final java.sql.Connection connection = Connection.getConnection();

        try {
            final PreparedStatement ps = connection.prepareStatement(query);
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

        final String query = String.format("UPDATE victims SET %s = ? WHERE %s = ?", updateColumn, whereColumn);

        boolean isVictimUpdated = false;
        final java.sql.Connection connection = Connection.getConnection();


        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newUpdateColumnValue);
            ps.setString(2, whereColumnValue);
            isVictimUpdated = ps.executeUpdate() == 1;
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

        return isVictimUpdated;
    }

    /**
     * `id` int(11) NOT NULL AUTO_INCREMENT,
     * `name` varchar(100),
     * `email` VARCHAR (150),
     * `phone` VARCHAR (20),
     * `fcm_id` text,
     * `fcm_updated_at` timestamp NULL,
     * `api_key` varchar(10) NOT NULL,
     * `imei` varchar(16) NOT NULL,
     * `actions` varchar(100) DEFAULT NULL,
     * `is_active` tinyint(4)   NOT NULL DEFAULT 1,
     * `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
     *
     * @param victim
     * @return
     @Override public boolean update(Victim victim) {

     boolean isUpdated = false;

     final String updateQuery = new PreparedUpdateQueryBuilder(TABLE_VICTIMS)
     .setIfNotNull(COLUMN_NAME, victim.getName())
     .setIfNotNull(COLUMN_EMAIL, victim.getEmail())
     .setIfNotNull(COLUMN_PHONE, victim.getPhone())
     .setIfNotNull(COLUMN_FCM_ID, victim.getFCMId())
     .setIfNotNull(victim.getFCMId(), COLUMN_FCM_UPDATED_AT, "CURRENT_TIMESTAMP", false)
     .setWhereClause("id = ?")
     .build();

     System.out.println("Query : " + updateQuery);

     final java.sql.Connection con = Connection.getConnection();
     try {
     final PreparedStatement ps = con.prepareStatement(updateQuery);

     } catch (SQLException e) {
     e.printStackTrace();
     } finally {
     try {
     con.close();
     } catch (SQLException e) {
     e.printStackTrace();
     }
     }

     return isUpdated;
     }*/
}
