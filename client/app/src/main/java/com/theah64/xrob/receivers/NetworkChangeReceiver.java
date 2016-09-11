package com.theah64.xrob.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.theah64.xrob.utils.ContactUtils;
import com.theah64.xrob.utils.NetworkUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final int MIN_THRESHOLD = 1000;
    private static final String X = NetworkChangeReceiver.class.getSimpleName();
    private static long lastUpdateTime = 0l;

    public NetworkChangeReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.hasNetwork(context)) {
            ContactUtils.push(context);
        }
    }
}
