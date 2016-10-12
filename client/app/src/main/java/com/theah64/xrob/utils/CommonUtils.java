package com.theah64.xrob.utils;

import android.os.Build;
import android.webkit.MimeTypeMap;

/**
 * Created by theapache64 on 11/9/16.
 */
public class CommonUtils {

    static boolean isSupport(final int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    public static int parseInt(String integer) {
        try {
            if (integer != null) {
                return Integer.parseInt(integer);
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String getContentTypeFromFile(String filePath) {
        String contentType = null;
        final String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (extension != null) {
            contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return contentType;
    }
}
