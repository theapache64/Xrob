package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 16/9/16,9:26 PM.
 */
public class File {

    private final String fileId, fileName, absoluteParentPath, fileSizeInKB, fileHash;
    private final boolean isDirectory;

    public File(String fileId, String fileName, String absoluteParentPath, String fileSizeInKB, String fileHash, boolean isDirectory) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.absoluteParentPath = absoluteParentPath;
        this.fileSizeInKB = fileSizeInKB;
        this.fileHash = fileHash;
        this.isDirectory = isDirectory;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSizeInKB() {
        return fileSizeInKB;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String getAbsoluteParentPath() {
        return absoluteParentPath;
    }

    public String getFileHash() {
        return fileHash;
    }
}
