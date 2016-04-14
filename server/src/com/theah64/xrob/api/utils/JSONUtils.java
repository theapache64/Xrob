package com.theah64.xrob.api.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by theapache64 on 11/22/2015.
 */
public class JSONUtils {

    public static final String KEY_ERROR = "error";
    public static final String KEY_MESSAGE = "message";
    private static final String ERROR_MESSAGE_UNAUTHORIZED_REQUEST = "Unauthorized request";


    private static String getSimpleResponse(final boolean isError, final String key, final String value) {
        final JSONObject errorObject = new JSONObject();
        try {
            errorObject.put(key, value);
            errorObject.put(KEY_ERROR, isError);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorObject.toString();
    }

    /**
     * Return JSON formatted error response
     *
     * @param errorMessage errorMessage to be displayed
     * @return JSONObject#toString
     */
    public static String getErrorJSON(String errorMessage) {
        return getSimpleResponse(true, KEY_MESSAGE, errorMessage);
    }


    /**
     * Return JSON formatted success response
     *
     * @param successMessage successMessage to be displayed
     * @return JSONObject#toString
     */
    public static String getSuccessJSON(String successMessage) {
        return getSimpleResponse(false, KEY_MESSAGE, successMessage);
    }

    /**
     * Return JSON String with error false and successKey as successValue
     *
     * @param successKey   successKey
     * @param successValue successValues corresponding to the given successKey
     * @return
     */
    public static String getSuccessJSON(final String successKey, String successValue) {
        return getSimpleResponse(false, successKey, successValue);
    }

    public static String getUnAuthorizedRequestError() {
        return getErrorJSON(ERROR_MESSAGE_UNAUTHORIZED_REQUEST);
    }
}
