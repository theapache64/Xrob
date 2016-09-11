package com.theah64.xrob.interfaces;

/**
 * Created by theapache64 on 11/9/16.
 */
public interface JobListener {
    void onJobStart();

    void onJobFinish(String data);

    void onJobFailed(String reason);
}
