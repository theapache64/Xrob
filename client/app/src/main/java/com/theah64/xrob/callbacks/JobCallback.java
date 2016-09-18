package com.theah64.xrob.callbacks;

/**
 * Created by theapache64 on 11/9/16.
 */
public interface JobCallback {
    void onJobStart();

    void onJobFinish(String data);

    void onJobFailed(String reason);
}
