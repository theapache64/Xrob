package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by theapache64 on 12/9/16.
 */
public abstract class BaseJSONPostNetworkAsyncTask<A, B, C> extends AsyncTask<A, B, C> {
    private final Context context;

    public BaseJSONPostNetworkAsyncTask(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
