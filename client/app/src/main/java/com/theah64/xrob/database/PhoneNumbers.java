package com.theah64.xrob.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.theah64.xrob.models.Contact;

import java.util.List;

/**
 * Created by theapache64 on 11/9/16.
 */
public class PhoneNumbers extends BaseTable<Contact.PhoneNumber> {

    private static final String COLUMN_CONTACT_ID = "contact_id";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_PHONE_TYPE = "phone_type";
    private static final String TABLE_PHONE_NUMBERS = "phone_numbers";
    private static PhoneNumbers instance;

    private PhoneNumbers(Context context) {
        super(context);
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
        rowId = db.insert(TABLE_PHONE_NUMBERS, null, cv);
        db.close();
        return rowId;
    }

    /**
     * To return non synchronized phone numbers
     *
     * @param contactId
     * @return
     */
    public List<Contact.PhoneNumber> getNonSyncedPhoneNumbers(final String contactId) {
        return null;
    }

}
