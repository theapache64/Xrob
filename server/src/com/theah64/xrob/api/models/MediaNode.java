package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 9/10/16.
 */
public class MediaNode {

    private static final String MEDIA_TYPE_FILE = "FILE";
    private static final String MEDIA_TYPE_VOICE = "VOICE";
    private static final String MEDIA_TYPE_SELFIE = "SELFIE";
    private static final String MEDIA_TYPE_SCREENSHOT = "SCREENSHOT";

    public static boolean isValidType(String type) {
        switch (type) {
            case MEDIA_TYPE_FILE:
            case MEDIA_TYPE_VOICE:
            case MEDIA_TYPE_SELFIE:
            case MEDIA_TYPE_SCREENSHOT:
                return true;
            default:
                return false;
        }
    }
}