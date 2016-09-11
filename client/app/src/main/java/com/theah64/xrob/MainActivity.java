package com.theah64.xrob;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.theah64.xrob.models.Contact;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadContacts();
    }

    private List<Contact> downloadContacts() {

        final Cursor contactCursor = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);


    }

}
