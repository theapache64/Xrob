package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class Victim {

    private String name;
    private String email;
    private String phone;
    private String fcmId;

    private final String id;
    private final String imei;
    private final String deviceHash;
    private final String apiKey;
    private final String deviceName;
    private final String otherDeviceInfo;
    private final String actions;
    private final String createdAt;
    private final boolean isActive;

    public Victim(String id, String name, String email, String phone, String imei, String deviceHash, String apiKey, String fcmId, String deviceName, String otherDeviceInfo, String actions, String createdAt, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imei = imei;
        this.deviceHash = deviceHash;
        this.apiKey = apiKey;
        this.fcmId = fcmId;
        this.deviceName = deviceName;
        this.otherDeviceInfo = otherDeviceInfo;
        this.actions = actions;
        this.createdAt = createdAt;
        this.isActive = isActive;
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

    public String getDeviceHash() {
        return deviceHash;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getOtherDeviceInfo() {
        return otherDeviceInfo;
    }
}
