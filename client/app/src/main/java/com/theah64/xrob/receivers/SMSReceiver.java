package com.theah64.xrob.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static android.view.View.X;

public class SMSReceiver extends BroadcastReceiver {

    private static final String X = SMSReceiver.class.getSimpleName();
    private static final String KEY_PDUS = "pdus";

    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(X, "SMS Received...");

        final Bundle dataBundle = intent.getExtras();

        if (dataBundle != null) {

            final Object[] pdus = (Object[]) dataBundle.get(KEY_PDUS);

            //Looping through  each pdus
            for (int i = 0; i < pdus.length; i++) {
                //TODO: Parse sms here and add it to the db with no sync flag.

            }
        }
    }
}
