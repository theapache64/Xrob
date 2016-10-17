package com.theah64.xrob.api.models;

/**
 * Created by shifar on 12/10/16.
 */
public class FTPServer {

    private final String id, name, ftpDomain, folderToSave, ftpUsername, ftpPassword;
    private final int totalMBUsed, freeSpaceInMb;

    public FTPServer(String id, String name, String ftpDomain, String folderToSave, String ftpUsername, String ftpPassword, int totalMBUsed, int freeSpaceInMb) {
        this.id = id;
        this.name = name;
        this.ftpDomain = ftpDomain;
        this.folderToSave = folderToSave;
        this.ftpUsername = ftpUsername;
        this.ftpPassword = ftpPassword;
        this.totalMBUsed = totalMBUsed;
        this.freeSpaceInMb = freeSpaceInMb;
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
}
