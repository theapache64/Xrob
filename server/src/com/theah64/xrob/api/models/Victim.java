package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class Victim {

    public static final int VICTIM_CODE_LENGTH = 10;
    private String name;
    private String email;
    private String phone;
    private String fcmId;

    private final String id;
    private final String imei;
    private final String deviceHash;
    private final String apiKey;
    private final String deviceName;
    private final String deviceInfoStatic, deviceInfoDynamic;
    private final String actions;
    private final String createdAt;
    private final boolean isActive;
    private final String victimCode;

    public Victim(String id, String name, String email, String phone, String imei, String deviceHash, String apiKey, String fcmId, String deviceName, String otherDeviceInfo, String deviceInfoDynamic, String actions, String createdAt, boolean isActive, String victimCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imei = imei;
        this.deviceHash = deviceHash;
        this.apiKey = apiKey;
        this.fcmId = fcmId;
        this.deviceName = deviceName;
        this.deviceInfoStatic = otherDeviceInfo;
        this.deviceInfoDynamic = deviceInfoDynamic;
        this.actions = actions;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.victimCode = victimCode;
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

    public String getDeviceInfoStatic() {
        return deviceInfoStatic;
    }

    public String getVictimCode() {
        return victimCode;
    }

    public static String getDeviceInfoReadable(String otherDeviceInfo) {

        try {
            final StringBuilder infoBuilder = new StringBuilder();

            final String[] deviceInfo = otherDeviceInfo.split(",");

            System.out.println("LENGHT: " + deviceInfo.length);

            for (final String info : deviceInfo) {
                final String[] labelInfo = info.split("=");
                System.out.print(" [" + info + "] ");
                System.out.println(labelInfo[0] + " = " + labelInfo[1]);
                infoBuilder.append("<b>").append(labelInfo[0]).append("</b> : ").append(labelInfo[1]).append("</br>");
            }

            return infoBuilder.toString();

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getIdentity() {
        if (getName() != null) {
            return getName();
        } else if (getEmail() != null) {
            return getEmail();
        } else {
            return getDeviceName();
        }
    }

    public String getDeviceInfoDynamic() {
        return deviceInfoDynamic;
    }
}
