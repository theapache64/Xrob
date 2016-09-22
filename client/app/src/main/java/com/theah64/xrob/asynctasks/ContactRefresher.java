package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.theah64.xrob.database.Contacts;
import com.theah64.xrob.database.PhoneNumbers;
import com.theah64.xrob.models.Contact;
import com.theah64.xrob.utils.APIRequestGateway;

/**
 * Created by theapache64 on 12/9/16.
 */
public class ContactRefresher extends AsyncTask<Void, Void, Boolean> {

    private static final String X = ContactRefresher.class.getSimpleName();

    private final Context context;

    public ContactRefresher(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        return refreshContacts(context);
    }

    private static synchronized boolean refreshContacts(Context context) {

        Log.i(X, "--------------------------------------");
        Log.i(X, "Refreshing contacts....");

        final Contacts contactsTable = Contacts.getInstance(context);
        final PhoneNumbers phoneNumbersTable = PhoneNumbers.getInstance(context);

        int totalAddedContacts = 0, totalAddedNumbers = 0, totalEditedContacts = 0;

        Log.i(X, "contactsTable and phoneNumbersTable are initialized...");

        final Cursor cCur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        Log.i(X, "Querying contacts....");

        if (cCur != null && cCur.moveToFirst()) {

            Log.d(X, cCur.getCount() + " contacts found");


            do {

                final String androidContactId = cCur.getString(cCur.getColumnIndex(ContactsContract.Contacts._ID));
                final String name = cCur.getString(cCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Contact contact = contactsTable.get(Contacts.COLUMN_ANDROID_CONTACT_ID, androidContactId);

                if (contact == null) {

                    contact = new Contact(null, androidContactId, name, null, false);
                    final long rowId = contactsTable.add(contact);

                    if (rowId != -1) {
                        totalAddedContacts++;
                        contact.setId(String.valueOf(rowId));
                    } else {
                        throw new IllegalArgumentException("Failed to add contact " + contact);
                    }

                } else {

                    if (!contact.getName().equals(name)) {

                        Log.d(X, "Contact name changed from : " + contact.getName() + " to " + name);

                        final boolean isEdited = contactsTable.update(Contacts.COLUMN_ANDROID_CONTACT_ID, androidContactId, Contacts.COLUMN_NAME, name);
                        totalEditedContacts++;

                        if (!isEdited) {
                            throw new IllegalArgumentException("Failed to change name");
                        }

                    }

                }

                final boolean hasPhoneNumbers = cCur.getInt(cCur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0;

                if (hasPhoneNumbers) {


                    final Cursor pCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{androidContactId}, null);

                    if (pCur != null && pCur.moveToFirst()) {


                        do {

                            final String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            final String phoneType = Contact.PhoneNumber.getTypeString(pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));

                            final boolean isExist = phoneNumbersTable.get(PhoneNumbers.COLUMN_PHONE_NUMBER, phone, PhoneNumbers.COLUMN_CONTACT_ID, contact.getId()) != null;

                            if (!isExist) {

                                totalAddedNumbers++;
                                final boolean isAdded = phoneNumbersTable.add(new Contact.PhoneNumber(contact.getId(), phone, phoneType)) != -1;

                                //Contact need to be synced
                                contactsTable.update(Contacts.COLUMN_ID, contact.getId(), Contacts.COLUMN_IS_SYNCED, "0");

                                if (!isAdded) {
                                    throw new IllegalArgumentException("Failed to add phone number of " + name + " - " + phone);
                                }


                            }

                        } while (pCur.moveToNext());

                    }


                    if (pCur != null) {
                        pCur.close();
                    }

                }


            } while (cCur.moveToNext());
        }

        if (cCur != null) {
            cCur.close();
        }

        Log.d(X, "Total added contacts : " + totalAddedContacts);
        Log.d(X, "Total added numbers : " + totalAddedNumbers);
        Log.d(X, "Total edited contacts : " + totalEditedContacts);

        Log.d(X, "--------------------------------------");

        return (totalAddedContacts + totalAddedNumbers + totalEditedContacts) > 0;

    }

    @Override
    protected void onPostExecute(Boolean isSyncNeeded) {
        Log.d(X, "isSyncNeeded : " + isSyncNeeded);

        if (isSyncNeeded) {

            new APIRequestGateway(context, new APIRequestGateway.APIRequestGatewayCallback() {

                @Override
                public void onReadyToRequest(String apiKey) {
                    new ContactsSynchronizer(context, apiKey).execute();
                }

                @Override
                public void onFailed(String reason) {
                    Log.e(X, "Failed to sync contacts : " + reason);
                }

            });
        }

    }
}
