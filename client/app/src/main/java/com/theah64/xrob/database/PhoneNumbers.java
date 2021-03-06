package com.theah64.xrob.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theah64.xrob.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 11/9/16.
 */
public class PhoneNumbers extends BaseTable<Contact.PhoneNumber> {

    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_PHONE_TYPE = "phone_type";
    private static PhoneNumbers instance;

    private PhoneNumbers(Context context) {
        super(context, "phone_numbers");
    }

    public static PhoneNumbers getInstance(final Context context) {

        if (instance == null) {
            instance = new PhoneNumbers(context);
        }

        return instance;
    }


    @Override
    public long add(Contact.PhoneNumber phoneNumber) {
        long rowId;
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues cv = new ContentValues(3);
        cv.put(COLUMN_CONTACT_ID, phoneNumber.getContactId());
        cv.put(COLUMN_PHONE_NUMBER, phoneNumber.getPhone());
        cv.put(COLUMN_PHONE_TYPE, phoneNumber.getPhoneType());
        rowId = db.insert(getTableName(), null, cv);

        return rowId;
    }

    @Override
    public Contact.PhoneNumber get(String column1, String value1, String column2, String value2) {
        Contact.PhoneNumber phoneNumber = null;

        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor cCur = db.query(getTableName(), new String[]{COLUMN_ID}, column1 + " = ?  AND " + column2 + " = ?", new String[]{value1, value2}, null, null, null, "1");

        if (cCur != null) {

            if (cCur.moveToFirst()) {
                final String id = cCur.getString(cCur.getColumnIndex(COLUMN_ID));
                phoneNumber = new Contact.PhoneNumber(id, null, null);
            }

            cCur.close();

        }

        return phoneNumber;
    }

    /**
     * To return non synchronized phone numbers. phone_number,phone_type
     *
     * @param contactId
     * @return
     */
    public List<Contact.PhoneNumber> getNonSyncedPhoneNumbers(final String contactId) {

        List<Contact.PhoneNumber> phoneNumbers = null;
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor pCur = db.query(getTableName(), new String[]{COLUMN_PHONE_NUMBER, COLUMN_PHONE_TYPE}, COLUMN_CONTACT_ID + " = ? AND is_synced = ?",
                new String[]{contactId, "0"}, null, null, null
        );

        if (pCur != null) {

            if (pCur.moveToFirst()) {

                phoneNumbers = new ArrayList<>(pCur.getCount());

                do {
                    final String phoneNumber = pCur.getString(pCur.getColumnIndex(COLUMN_PHONE_NUMBER));
                    final String phoneType = pCur.getString(pCur.getColumnIndex(COLUMN_PHONE_TYPE));

                    phoneNumbers.add(new Contact.PhoneNumber(contactId, phoneNumber, phoneType));

                } while (pCur.moveToNext());

            }

            pCur.close();
        }


        return phoneNumbers;
    }

}
