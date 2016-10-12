package com.theah64.xrob.asynctasks;

import android.content.Context;

/**
 * Created by theapache64 on 12/10/16.
 */

public abstract class BaseQueueSynchronizer<P> extends BaseJSONPostNetworkAsyncTask<Void> {
    BaseQueueSynchronizer(Context context, String apiKey) {
        super(context, apiKey);
    }
}
