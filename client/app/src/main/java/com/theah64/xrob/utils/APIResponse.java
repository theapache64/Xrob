package com.theah64.xrob.utils;

import android.support.annotation.StringRes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shifar on 23/7/16.
 */
public class APIResponse {

    private static final String KEY_ERROR = "error";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DATA = "data";
    private static final String KEY_ERROR_CODE = "error_code";
    private final String message;
    private final JSONObject joMain;

    public APIResponse(final String stringResp) throws JSONException, APIException {

        joMain = new JSONObject(stringResp);
        this.message = joMain.getString(KEY_MESSAGE);

        if (joMain.getBoolean(KEY_ERROR)) {
            final int errorCode = joMain.getInt(KEY_ERROR_CODE);
            throw new APIException(errorCode, message);
        }

    }

    public JSONObject getJSONObjectData() throws JSONException {
        return joMain.getJSONObject(KEY_DATA);
    }


    public String getMessage() {
        return this.message;
    }

    public static class APIException extends Exception {

        private final int pleasantMsg;

        public APIException(final int errorCode, String msg) {
            super(msg);
            pleasantMsg = getPleasantMessage(errorCode);
        }

        private static
        @StringRes
        int getPleasantMessage(int errorCode) {

            switch (errorCode) {

                default:
                    return -1;
            }

        }

        @StringRes
        public int getPleasantMsg() {
            return pleasantMsg;
        }
    }

}
