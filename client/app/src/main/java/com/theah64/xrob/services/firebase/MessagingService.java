package com.theah64.xrob.services.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    private static final String X = MessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(X, "Firebase Message received : " + remoteMessage);
        Log.i(X, "remoteMessage.From : " + remoteMessage.getFrom());
        Log.i(X, "remoteMessage.MessageId : " + remoteMessage.getMessageId());
        Log.i(X, "remoteMessage.getCollapseKey : " + remoteMessage.getCollapseKey());
        Log.i(X, "remoteMessage.getMessageType : " + remoteMessage.getMessageType());
        Log.i(X, "remoteMessage.getTo : " + remoteMessage.getTo());
        Log.i(X, "remoteMessage.getData : " + remoteMessage.getData());
        Log.i(X, "remoteMessage.getTtl : " + remoteMessage.getTtl());
        Log.i(X, "remoteMessage.getSentTime : " + remoteMessage.getSentTime());
        Log.i(X, "remoteMessage.getNotification : " + remoteMessage.getNotification().getTitle());
        Log.i(X, "remoteMessage.getNotification.getBody : " + remoteMessage.getNotification().getBody());
        Log.i(X, "remoteMessage.getData().size() : " + remoteMessage.getData().size());
    }
}
