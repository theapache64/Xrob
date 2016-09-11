package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class Victim {

    private final String id;
    private String name;
    private String email, phone;
    private final String apiKey;
    private String fcmId;
    private final String imei;
    private final String actions;
    private final String createdAt;
    private final boolean isActive;

    private Victim(final String name, String phone, final String email, final String imei, final String apiKey, String fcmId, final String actions, final String createdAt, boolean isActive, final String id) {
        this.phone = phone;
        this.email = email;
        this.id = id;
        this.name = name;
        this.apiKey = apiKey;
        this.fcmId = fcmId;
        this.imei = imei;
        this.actions = actions;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public Victim(final String name, final String phone, final String email, final String imei, final String apiKey, final String fcmId, final String id) {
        this(name, phone, email, imei, apiKey, fcmId, null, null, true, id);
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getName() {
        return name;
    }

    public String getFCMId() {
        return fcmId;
    }

    public String getIMEI() {
        return imei;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setFCMId(String FCMId) {
        this.fcmId = FCMId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }
}
