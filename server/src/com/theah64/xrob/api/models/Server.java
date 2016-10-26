package com.theah64.xrob.api.models;

/**
 * Created by shifar on 12/10/16.
 */
public class Server {

    private final String id, name, ftpDomain, folderToSave, ftpUsername, ftpPassword, uploadScriptFile;
    private final int totalMBUsed, freeSpaceInMb;
    private String uploadUrl;

    public Server(String id, String name, String ftpDomain, String folderToSave, String ftpUsername, String ftpPassword, String uploadScriptFile, int totalMBUsed, int freeSpaceInMb) {
        this.id = id;
        this.name = name;
        this.ftpDomain = ftpDomain;
        this.folderToSave = folderToSave;
        this.ftpUsername = ftpUsername;
        this.ftpPassword = ftpPassword;
        this.uploadScriptFile = uploadScriptFile;
        this.totalMBUsed = totalMBUsed;
        this.freeSpaceInMb = freeSpaceInMb;
        this.uploadUrl = "http://" + ftpDomain + "/" + uploadScriptFile;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFtpDomain() {
        return ftpDomain;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public String getFolderToSave() {
        return folderToSave;
    }

    public int getTotalMBUsed() {
        return totalMBUsed;
    }

    public int getFreeSpaceInMb() {
        return freeSpaceInMb;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ftpDomain='" + ftpDomain + '\'' +
                ", folderToSave='" + folderToSave + '\'' +
                ", ftpUsername='" + ftpUsername + '\'' +
                ", ftpPassword='" + ftpPassword + '\'' +
                ", uploadScriptFile='" + uploadScriptFile + '\'' +
                ", totalMBUsed=" + totalMBUsed +
                ", freeSpaceInMb=" + freeSpaceInMb +
                ", uploadUrl='" + uploadUrl + '\'' +
                '}';
    }
}
