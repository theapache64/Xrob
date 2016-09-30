package com.theah64.xrob.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.theah64.xrob.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 30/9/16.
 */

public class Messages extends BaseTable<Message> {

    public static final String COLUMN_ANDROID_MESSAGE_ID = "android_message_id";
    public static final String COLUMN_FROM = "_from";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TYPE = "_type";
    public static final String COLUMN_DELIVERY_TIME = "delivery_time";
    private static final String X = Messages.class.getSimpleName();
    private static Messages instance;

    private Messages(Context context) {
        super(context, "messages");
    }

    public static Messages getInstance(Context context) {
        if (instance == null) {
            instance = new Messages(context);
        }
        return instance;
    }

    @Override
    public long add(Message message) {

        Log.d(X, "Adding message : " + message);

        final ContentValues cv = new ContentValues(6);

        cv.put(COLUMN_ANDROID_MESSAGE_ID, message.getAndroidId());
        cv.put(COLUMN_FROM, message.getFrom());
        cv.put(COLUMN_CONTENT, message.getContent());
        cv.put(COLUMN_TYPE, message.getType());
        cv.put(COLUMN_DELIVERY_TIME, message.getDeliveryTime());
        cv.put(COLUMN_CREATED_AT, System.currentTimeMillis());

        return this.getWritableDatabase().insert(getTableName(), null, cv);
    }

    @Override
    public List<Message> getAll() {
        List<Message> messages = null;
        final Cursor c = this.getReadableDatabase().query(getTableName(),
                new String[]{COLUMN_ANDROID_MESSAGE_ID, COLUMN_FROM, COLUMN_CONTENT, COLUMN_TYPE, COLUMN_DELIVERY_TIME}, null, null, null, null, COLUMN_ID);

        if (c != null) {

            if (c.moveToFirst()) {

                messages = new ArrayList<>(c.getCount());

                do {
                    final int androidMessageId = c.getInt(c.getColumnIndex(COLUMN_ANDROID_MESSAGE_ID));
                    final String from = c.getString(c.getColumnIndex(COLUMN_FROM));
                    final String content = c.getString(c.getColumnIndex(COLUMN_CONTENT));
                    final String type = c.getString(c.getColumnIndex(COLUMN_TYPE));
                    final long deliveryTime = c.getLong(c.getColumnIndex(COLUMN_DELIVERY_TIME));

                    messages.add(new Message(androidMessageId, from, content, type, deliveryTime));

                } while (c.moveToNext());

            }

            c.close();
        }

        return messages;
    }
}
