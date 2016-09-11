package com.theah64.xrob;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.theah64.xrob.database.Contacts;
import com.theah64.xrob.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String X = MainActivity.class.getSimpleName();
    private Contacts contactsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsTable = Contacts.getInstance(this);
        List<Contact> contacts = downloadContacts();

        for (final Contact contact : contacts) {
            Log.d(X, "-------------------------------------------");
            Log.d(X, "Contact : " + contact);
            if (contact.getPhoneNumbers() != null) {
                for (final Contact.PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
                    Log.d(X, "Number : " + phoneNumber);
                }
            }
            Log.d(X, "-------------------------------------------");
        }
    }

    private List<Contact> downloadContacts() {

        List<Contact> contacts = null;

        final Cursor cCur = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cCur != null && cCur.moveToFirst()) {

            contacts = new ArrayList<>(cCur.getCount());

            do {

                final String id = cCur.getString(cCur.getColumnIndex(ContactsContract.Contacts._ID));
                final String name = cCur.getString(cCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Contact contact = contactsTable.

                final Contact contact = new Contact(null, id, name, null, false);
                final String contactId = contactsTable.add(contact);

                if (contactId != null) {
                    contact.setId(contactId);

                    final boolean hasPhoneNumbers = cCur.getInt(cCur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0;

                    if (hasPhoneNumbers) {

                        final Cursor pCur = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);

                        if (pCur != null && pCur.moveToFirst()) {

                            final List<Contact.PhoneNumber> phoneNumbers = new ArrayList<>(pCur.getCount());

                            do {
                                final String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                final String phoneType = Contact.PhoneNumber.getTypeString(pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));

                                phoneNumbers.add(new Contact.PhoneNumber(contactId, phone, phoneType));

                            } while (pCur.moveToNext());

                            contact.setPhoneNumbers(phoneNumbers);
                        }


                        if (pCur != null) {
                            pCur.close();
                        }

                    }

                    contacts.add(contact);

                } else {
                    throw new IllegalArgumentException("Failed to add contact " + contact);
                }


            } while (cCur.moveToNext());
        }

        if (cCur != null) {
            cCur.close();
        }

        return contacts;
    }

}
