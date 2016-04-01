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
}
