package com.theah64.xrob.asynctasks;

import android.content.Context;

import com.theah64.xrob.database.PendingDeliveries;
import com.theah64.xrob.models.PendingDelivery;
import com.theah64.xrob.utils.OkHttpUtils;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

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

        final List<PendingDelivery> pendingDeliveryList = PendingDeliveries.getInstance(getContext()).getAll();
        if (pendingDeliveryList != null) {
            //Looping through each delivery.
            for (final PendingDelivery pd : pendingDeliveryList) {

                

            }
        }
        return null;
    }
}
