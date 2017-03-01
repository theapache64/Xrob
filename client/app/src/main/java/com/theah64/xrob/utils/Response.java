package com.theah64.xrob.utils;

import android.support.annotation.StringRes;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shifar on 23/7/16.
 */
public class Response {

    public static final String KEY_MESSAGE = "message";
    public static final String KEY_ERROR = "error";
    public static final String KEY_ERROR_CODE = "error_code";
    public static final String KEY_DATA = "data";
    private final String message;
    private final JSONObject joMain;

    public Response(final String stringResp) throws ResponseException, JSONException {

        try {
            joMain = new JSONObject(stringResp);
            this.message = joMain.getString(KEY_MESSAGE);

            if (joMain.getBoolean(KEY_ERROR)) {
                final int errorCode = joMain.getInt(KEY_ERROR_CODE);
                throw new ResponseException(errorCode, message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            throw e;
        }

    }

    JSONObject getJSONObjectData() throws JSONException {
        return joMain.getJSONObject(KEY_DATA);
    }


    public String getMessage() {
        return this.message;
    }

    public static class ResponseException extends Exception {

        static final int ERROR_CODE_INVALID_TYPE = 1;
        static final int ERROR_CODE_INVALID_USER_ID = 3;
        static final int ERROR_CODE_NO_LISTENER_FOUND = 2;
        static final int ERROR_CODE_TELLER_CLOSED = 4;
        static final int ERROR_CODE_FCM_FIRE_FAILED = 6;


        private final int errorCode;
        private final int pleasantMsg;

        ResponseException(final int errorCode, String msg) {
            super(msg);
            pleasantMsg = getPleasantMessage(errorCode);
            this.errorCode = errorCode;
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


        int getErrorCode() {
            return errorCode;
        }
    }

}
