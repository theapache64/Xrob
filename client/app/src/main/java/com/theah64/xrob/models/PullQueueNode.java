package com.theah64.xrob.models;

/**
 * Created by theapache64 on 12/10/16.
 */
public class PullQueueNode {

    private final String id, filePath;
    private final long capturedAt;

    public PullQueueNode(String id, String filePath, long capturedAt) {
        this.id = id;
        this.filePath = filePath;
        this.capturedAt = capturedAt;
    }

    public String getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getCapturedAt() {
        return capturedAt;
    }
}
