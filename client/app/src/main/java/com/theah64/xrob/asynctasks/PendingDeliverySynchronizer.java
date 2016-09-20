package com.theah64.xrob.asynctasks;

import android.content.Context;

/**
 * Created by theapache64 on 21/9/16.
 */
public class PendingDeliverySynchronizer extends BaseJSONPostNetworkAsyncTask<Void> {

    public PendingDeliverySynchronizer(Context context, final String apiKey) {
        super(context, apiKey);
    }

    @Override
    protected Void doInBackground(String... strings) {
        //TODO : SQL to OK HTTP
        return null;
    }
}
