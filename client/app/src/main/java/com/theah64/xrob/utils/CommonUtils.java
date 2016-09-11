package com.theah64.xrob.utils;

import android.os.Build;

/**
 * Created by theapache64 on 11/9/16.
 */
public class CommonUtils {
    public static boolean isSupport(final int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }
}
