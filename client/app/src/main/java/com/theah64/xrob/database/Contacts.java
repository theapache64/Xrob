package com.theah64.xrob.database;

import android.content.Context;
import android.database.ContentObserver;

import com.theah64.xrob.models.Contact;

import java.util.List;

/**
 * Created by theapache64 on 4/9/16.
 */
public class Contacts extends BaseTable<Contact> {

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


    @Override
    public List<Contact> getAll() {
        List<Contact> contacts = null;
        
        return contacts;
    }
}
