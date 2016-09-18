package com.theah64.xrob.services.firebase;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.theah64.xrob.R;
import com.theah64.xrob.commandcenter.CommandCenter;

import java.util.Map;

public class CommandReceiverService extends FirebaseMessagingService {

    private static final String X = CommandReceiverService.class.getSimpleName();
    private static final String KEY_TYPE = "type";
    private static final String TYPE_COMMAND = "command";
    private static final String KEY_DATA = "data";
    private static final String KEY_TYPE_DATA = "type_data";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> payload = remoteMessage.getData();
        Log.i(X, "FCM says : " + payload);

        if (!payload.isEmpty()) {

            final String type = payload.get(KEY_TYPE);

            if (type.equals(TYPE_COMMAND)) {
                Log.d(X, "Command is gonna handle...");
                CommandCenter.handle(this, payload.get(KEY_TYPE_DATA));
            }
        }
    }

    private void showNotification(Context context, String command) {
        final NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context)
                .setContentTitle(command)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_error_outline_black_48dp);

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notificationCompat.build());
    }
}
