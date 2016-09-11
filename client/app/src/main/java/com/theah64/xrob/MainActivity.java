package com.theah64.xrob;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.theah64.xrob.database.Contacts;
import com.theah64.xrob.database.PhoneNumbers;
import com.theah64.xrob.models.Contact;
import com.theah64.xrob.services.ContactsService;
import com.theah64.xrob.utils.ContactUtils;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String X = MainActivity.class.getSimpleName();
    private static final int RQ_CODE_RQ_PERMISSIONS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, RQ_CODE_RQ_PERMISSIONS);
            } else {
                doNormalWork();
            }

        } else {
            doNormalWork();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RQ_CODE_RQ_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doNormalWork();
            } else {
                Toast.makeText(MainActivity.this, "You must accept the permissions.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void doNormalWork() {
        startService(new Intent(this, ContactsService.class));
        ContactUtils.refreshContacts(this);
    }

}
