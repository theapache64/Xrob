package com.theah64.xrob.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theah64.xrob.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 4/9/16.
 */
public class Contacts extends BaseTable<Contact> {

    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PHONE_TYPE = "phone_type";
    private static Contacts instance;

    public static Contacts getInstance(final Context context) {

        if (instance == null) {
            instance = new Contacts(context);
        }

        return instance;
    }

    private Contacts(Context context) {
        super(context);
    }

    /**
     * Used to retrieve all un-synchronized contacts from local database.
     */
    public List<Contact> getAllUnSynced() {

        List<Contact> contacts = null;
        //Preparing query
        final String query = "SELECT id,name,phone,phone_type FROM contacts WHERE is_synced = 0;";

        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            contacts = new ArrayList<>(cursor.getCount());

            do {
                final String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                final String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                final String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
                final String phoneType = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_TYPE));


                //adding contact to the list
                contacts.add(new Contact(id, name, phone, phoneType, false));

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return contacts;

    }
}
