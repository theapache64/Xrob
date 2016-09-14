package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 11/9/16,2:06 PM.
 */
public class Client {

    public static final String KEY = "client";
    private final String id;
    private String username;
    private final String passHash;
    private final String apiKey;
    private String email;
    private String clientCode;

    public Client(String id, String username, String passHash, String apiKey, String email, String clientCode) {
        this.id = id;
        this.username = username;
        this.passHash = passHash;
        this.apiKey = apiKey;
        this.email = email;
        this.clientCode = clientCode;
    }

    public String getId() {
        return id;
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

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", passHash='" + passHash + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", email='" + email + '\'' +
                ", clientCode='" + clientCode + '\'' +
                '}';
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }
}
