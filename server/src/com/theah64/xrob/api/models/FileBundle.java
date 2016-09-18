package com.theah64.xrob.api.models;

import com.theah64.xrob.api.utils.clientpanel.TimeUtils;

/**
 * Created by theapache64 on 18/9/16,4:37 PM.
 */
public class FileBundle {
    private final String id, victimId, relativeSyncedTime, bundleHash;

    public FileBundle(String id, String victimId, long epochTime, String bundleHash) {
        this.id = id;
        this.victimId = victimId;
        this.bundleHash = bundleHash;
        this.relativeSyncedTime = epochTime > 0 ? TimeUtils.getRelativeTime(false, epochTime) : null;
    }

    public String getId() {
        return id;
    }

    public String getRelativeSyncedTime() {
        return relativeSyncedTime;
    }

    public String getVictimId() {
        return victimId;
    }

    public String getBundleHash() {
        return bundleHash;
    }
}
