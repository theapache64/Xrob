package com.theah64.xrob.database;

import android.content.ContentValues;
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
    public static final String COLUMN_ANDROID_CONTACT_ID = "android_contact_id";
    public static final String COLUMN_IS_SYNCED = "is_synced";
    private static Contacts instance;

    public static Contacts getInstance(final Context context) {

        if (instance == null) {
            instance = new Contacts(context);
        }

        return instance;
    }

    private Contacts(Context context) {
        super(context, "contacts");
    }


    /**
     * Used to retrieve all un-synchronized contacts from local database.
     */
    public List<Contact> getNonSyncedContacts() {

        List<Contact> contacts = null;

        //Preparing query

        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(getTableName(), new String[]{COLUMN_ID, COLUMN_ANDROID_CONTACT_ID, COLUMN_NAME}, COLUMN_IS_SYNCED + "= ?",
                new String[]{FALSE}, null, null, null
        );

        final PhoneNumbers phoneNumbersTable = PhoneNumbers.getInstance(getContext());

        if (cursor.moveToFirst()) {

            contacts = new ArrayList<>(cursor.getCount());

            do {

                final String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                final String androidContactId = cursor.getString(cursor.getColumnIndex(COLUMN_ANDROID_CONTACT_ID));
                final String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

                final List<Contact.PhoneNumber> phoneNumbers = phoneNumbersTable.getNonSyncedPhoneNumbers(id);

                //adding contact to the list
                contacts.add(new Contact(id, androidContactId, name, phoneNumbers, false));

            } while (cursor.moveToNext());

        }

        cursor.close();


        return contacts;
    }

    @Override
    public long add(Contact contact) {
        long newContactId;
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues cv = new ContentValues(1);
        cv.put(COLUMN_NAME, contact.getName());
        cv.put(COLUMN_ANDROID_CONTACT_ID, contact.getAndroidContactId());
        newContactId = db.insert(getTableName(), null, cv);

        return newContactId;
    }

    @Override
    public Contact get(String whereColumn, String whereColumnValue) {

        Contact contact = null;

        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor cCur = db.query(getTableName(), new String[]{COLUMN_ID, COLUMN_NAME}, whereColumn + " = ? ", new String[]{whereColumnValue}, null, null, null, "1");

        if (cCur != null) {

            if (cCur.moveToFirst()) {
                final String id = cCur.getString(cCur.getColumnIndex(COLUMN_ID));
                final String name = cCur.getString(cCur.getColumnIndex(COLUMN_NAME));
                contact = new Contact(id, null, name, null, false);
            }

            cCur.close();

        }

        return contact;
    }

    @Override
    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {
        boolean isEdited;
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues cv = new ContentValues(2);
        cv.put(updateColumn, newUpdateColumnValue);
        cv.put(COLUMN_IS_SYNCED, "0");
        isEdited = db.update(getTableName(), cv, whereColumn + " = ? ", new String[]{whereColumnValue}) > 0;

        return isEdited;
    }

    public void setAllContactsAndNumbersSynced() {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE contacts SET is_synced = 1 WHERE is_synced = 0;");
        db.execSQL("UPDATE phone_numbers SET is_synced = 1 WHERE is_synced = 0;");

    }
}
