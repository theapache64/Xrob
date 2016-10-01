package com.theah64.xrob.models;

/**
 * Created by theapache64 on 21/9/16.
 */
public class PendingDelivery {
    private final boolean isError;
    private final String id, dataType, data, message;

    public PendingDelivery(String id, boolean isError, String dataType, String data, String message) {
        this.id = id;
        this.isError = isError;
        this.dataType = dataType;
        this.data = data;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public boolean isError() {
        return isError;
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

    @Override
    public String toString() {
        return "PendingDelivery{" +
                "isError=" + isError +
                ", id='" + id + '\'' +
                ", dataType='" + dataType + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
