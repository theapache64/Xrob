package com.theah64.xrob.database;

import android.content.Context;

import com.theah64.xrob.models.Contact;

import java.util.List;

/**
 * Created by theapache64 on 11/9/16.
 */
public class PhoneNumbers extends BaseTable<Contact.PhoneNumber> {

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
