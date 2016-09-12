package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 11/9/16,2:06 PM.
 */
public class Client {

    private final String username, passHash, apiKey, email, clientCode;

    public Client(String username, String passHash, String apiKey, String email, String clientCode) {
        this.username = username;
        this.passHash = passHash;
        this.apiKey = apiKey;
        this.email = email;
        this.clientCode = clientCode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassHash() {
        return passHash;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getEmail() {
        return email;
    }

    public String getClientCode() {
        return clientCode;
    }
}
