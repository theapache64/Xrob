package com.theah64.xrob.api.utils;

/**
 * Created by shifar on 31/12/15.
 */
public class CommonUtils {
    public static boolean isNumeric(final String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getProperSentense(int count, String singular, String plural) {
        return count <= 1 ? singular : plural;
    }

    public static String hyphenIfNull(String data) {
        return data == null ? "-" : data;
    }

    public static String emptyIfNull(String data) {
        return data == null ? "" : data;
    }
}
