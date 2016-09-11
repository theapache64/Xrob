package com.theah64.xrob.services;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import com.theah64.xrob.utils.ContactUtils;

public class ContactsService extends Service {

    private static final int MIN_THRESHOLD = 1000;
    private static long lastTimeOfUpdate = 0;


    public ContactsService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private MyContactObserver contactObserver = new MyContactObserver(null);

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Contact observer registered");
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, contactObserver);
    }

    public class MyContactObserver extends ContentObserver {

        public MyContactObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            System.out.println("Contact changed");

            //To prevent double call
            if ((System.currentTimeMillis() - lastTimeOfUpdate) > MIN_THRESHOLD) {
                System.out.println("Real code works here");
                ContactUtils.refreshContacts(getApplicationContext());
                lastTimeOfUpdate = System.currentTimeMillis();
            } else {
                System.out.println("Not now!");
            }

        }

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
}
