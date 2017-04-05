package com.theah64.xrob.models;


import com.theah64.xrob.utils.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

/**
 * Created by theapache64 on 17/1/17.
 */

public class SocketMessage {

    private static final String KEY_DEVICE_TIME = "device_time";

    private final JSONObject joSocketMessage;

    public SocketMessage(String message, boolean isError) throws JSONException, IOException {

        joSocketMessage = new JSONObject();
        joSocketMessage.put(Response.KEY_MESSAGE, message);
        joSocketMessage.put(Response.KEY_ERROR, isError);
        joSocketMessage.put(KEY_DEVICE_TIME, new Date());


        final JSONObject joData = new JSONObject();

        joSocketMessage.put(Response.KEY_DATA, joData);

    }


    @Override
    public String toString() {
        return joSocketMessage.toString();
    }
}
