package com.theah64.xrob.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by theapache64 on 11/9/16.
 */
public class PrefUtils {

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


    public SharedPreferences.Editor getEditor() {
        return sharedPref.edit();
    }

    public String getString(String key) {
        return this.sharedPref.getString(key, null);
    }

    public boolean getBoolean(String key) {
        return this.sharedPref.getBoolean(key, false);
    }
}
