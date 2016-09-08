package com.theah64.xrob.services;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactsService extends Service {

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
            System.out.println("Contact changed");
            super.onChange(selfChange, uri);
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
