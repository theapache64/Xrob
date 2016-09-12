package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 11/9/16,2:06 PM.
 */
public class Clients extends BaseTable<Client> {
    public static final String COLUMN_CLIENT_CODE = "client_code";

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
}
