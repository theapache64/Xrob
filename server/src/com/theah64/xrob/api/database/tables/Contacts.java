package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Contact;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by theapache64 on 4/9/16,11:39 AM.
 */
public class Contacts extends BaseTable<Contact> {

    private static Contacts instance = new Contacts();

    private Contacts() {
    }

    public static Contacts getInstance() {
        return instance;
    }

    @Override
    public void addv2(@Nullable String userId, JSONArray jsonArray) throws RuntimeException {
        final String query = "INSERT INTO contacts (user_id, name, number) VALUES (?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
