package com.theah64.xrob.models;

/**
 * Created by theapache64 on 21/9/16.
 */
public class PendingDelivery {
    private final String id, dataType, data, message;

    public PendingDelivery(String id, String dataType, String data, String message) {
        this.id = id;
        this.dataType = dataType;
        this.data = data;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getDataType() {
        return dataType;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
