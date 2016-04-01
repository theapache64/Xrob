package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class User {

    private final String id;
    private final String name;
    private final String apiKey;
    private final String gcmId;
    private final String imei;
    private final String actions;
    private final String createdAt;
    private final boolean isActive;

    private User(final String name, final String imei, final String apiKey, String gcmId, final String actions, final String createdAt, boolean isActive, final String id) {
        this.id = id;
        this.name = name;
        this.apiKey = apiKey;
        this.gcmId = gcmId;
        this.imei = imei;
        this.actions = actions;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public User(final String name, final String imei, final String apiKey, final String gcmId) {
        this(name, imei, apiKey, gcmId, null, null, true, null);
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getName() {
        return name;
    }

    public String getGCMId() {
        return gcmId;
    }

    public String getIMEI() {
        return imei;
    }
}
