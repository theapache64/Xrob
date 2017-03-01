package com.theah64.xrob.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.theah64.xrob.activities.MainActivity;

public class OutgoingCallReceiver extends BroadcastReceiver {

    private static final String X = OutgoingCallReceiver.class.getSimpleName();
    private static final String LAUNCH_CODE = "753159";

    public OutgoingCallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String phoneNumber = getResultData();
        Log.d(X, "Result data phone number is : " + phoneNumber);

        if (phoneNumber == null) {
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(X, "Intent phone number : " + phoneNumber);
        }

        if (phoneNumber.equals(LAUNCH_CODE)) {
            Log.i(X, "Launch code matched");

            //Showing launcher icon
            PackageManager p = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, MainActivity.class);
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            final Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        }

    }
}
