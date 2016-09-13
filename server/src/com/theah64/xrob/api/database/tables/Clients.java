package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Client;
import com.theah64.xrob.api.models.ClientVictimRelation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 11/9/16,2:06 PM.
 */
public class Clients extends BaseTable<Client> {

    public static final String COLUMN_CLIENT_CODE = "client_code";
    public static final String COLUMN_USERNAME = "username";
    public static final java.lang.String COLUMN_PASS_HASH = "pass_hash";

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
        boolean isExist = false;
        final String query = "SELECT id FROM clients WHERE client_code = ? LIMIT 1";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, client.getClientCode());
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

    @Override
    public boolean add(Client client) {

        final String addVictimQuery = "INSERT INTO clients (username, pass_hash, api_key,email,client_code) VALUES (?,?,?,?,?);";
        final java.sql.Connection connection = Connection.getConnection();

        //To track the success
        boolean isClientAdded = false;

        try {
            final PreparedStatement ps = connection.prepareStatement(addVictimQuery);

            ps.setString(1, client.getUsername());
            ps.setString(2, client.getPassHash());
            ps.setString(3, client.getApiKey());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getClientCode());

            isClientAdded = ps.executeUpdate() == 1;

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

        return isClientAdded;
    }

    @Override
    public Client get(String column1, String value1, String column2, String value2) {

        final String query = String.format("SELECT id FROM clients WHERE %s = ? AND %s = ? LIMIT 1", column1, column2);

        final java.sql.Connection connection = Connection.getConnection();
        Client client = null;

        try {

            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, value1);
            ps.setString(2, value2);

            final ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //Collecting relation
                final String id = rs.getString(COLUMN_ID);
                client = new Client(id, null, null, null, null, null);
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

        return client;
    }
}
