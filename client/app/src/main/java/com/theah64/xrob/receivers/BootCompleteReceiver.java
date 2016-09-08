package com.theah64.xrob.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.theah64.xrob.services.ContactsService;

/**
 * Created by theapache64 on 4/9/16.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private static final String X = BootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(X, "Boot completed, starting initial jobs.");
        context.startService(new Intent(context, ContactsService.class));
    }


}
