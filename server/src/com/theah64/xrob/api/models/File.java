package com.theah64.xrob.api.models;

import java.util.List;

/**
 * Created by theapache64 on 16/9/16,9:26 PM.
 */
public class File {

    private final String id, fileName, absoluteParentPath, fileSizeInKB;
    private final boolean isDirectory;

    public File(String id, String fileName, String absoluteParentPath, String fileSizeInKB, boolean isDirectory) {
        this.id = id;
        this.fileName = fileName;
        this.absoluteParentPath = absoluteParentPath;
        this.fileSizeInKB = fileSizeInKB;
        this.isDirectory = isDirectory;
    }

    public String getId() {
        return id;
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
}
