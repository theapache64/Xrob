package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Contact;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 4/9/16,11:39 AM.
 */
public class Contacts extends BaseTable<Contact> {

    private static final String COLUMN_PHONE = "phone";
    private static Contacts instance = new Contacts();

    private Contacts() {
    }

    public static Contacts getInstance() {
        return instance;
    }

    @Override
    protected boolean isExist(Contact contact) {

        boolean isExist = false;
        final String query = "SELECT id FROM contacts WHERE name = ? AND phone = ? AND user_id = ? LIMIT 1";
        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, contact.getName());
            ps.setString(2, contact.getPhone());
            ps.setString(3, contact.getUserId());

            final ResultSet rs = ps.executeQuery();
            isExist = rs.first();
            System.out.println("isExist : " + isExist + ", Contact : " + contact);
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
    protected List<Contact> parse(@Nullable String userId, @NotNull JSONArray jaContacts) throws JSONException {

        final List<Contact> contactList = new ArrayList<>();

        for (int i = 0; i < jaContacts.length(); i++) {

            final JSONObject joContact = jaContacts.getJSONObject(i);

            final String name = joContact.getString(COLUMN_NAME);
            final String phone = joContact.getString(COLUMN_PHONE);
            contactList.add(new Contact(userId, name, phone));

        }

        return contactList;
    }

    @Override
    public void addv2(@Nullable String userId, @NotNull JSONArray jsonArray) throws RuntimeException, JSONException {

        boolean isAdded = true;
        final List<Contact> contactList = parse(userId, jsonArray);
        final String query = "INSERT INTO contacts (user_id, name, phone) VALUES (?,?,?);";

        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);

            for (final Contact contact : contactList) {
                if (!isExist(contact)) {
                    //Adding new contact

                    ps.setString(2, contact.getName());
                    ps.setString(3, contact.getPhone());
                    isAdded = isAdded && ps.executeUpdate() == 1;
                } else {
                    System.out.println("Exists: " + contact);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (!isAdded) {
            throw new RuntimeException("Error occured while adding contacts to database");
        }

    }
}
