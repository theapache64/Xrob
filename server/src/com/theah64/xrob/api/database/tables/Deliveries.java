package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Delivery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * To manage server data deliveries
 * Created by theapache64 on 11/29/2015.
 */
public class Deliveries extends BaseTable<Delivery> {

    private static Deliveries instance = new Deliveries();

    private Deliveries() {
    }

    public static Deliveries getInstance() {
        return instance;
    }

    @Override
    public Delivery get(String column, String value) {
        return null;
    }

    @Override
    public boolean add(Delivery delivery) {

        boolean isAdded = false;

        final String query = "INSERT INTO deliveries (user_id,error,message,server_error, server_error_message,data_type) VALUES (?,?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);


            ps.setString(1, delivery.getUserId());
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
    public void addv2(Delivery delivery) throws Exception {
        if (!add(delivery)) {
            throw new Exception("Failed to add delivery details");
        }
    }
}
