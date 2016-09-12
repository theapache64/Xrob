package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 11/9/16,2:06 PM.
 */
public class Client {
    private final String clientCode;

    public Client(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientCode() {
        return clientCode;
    }
}
