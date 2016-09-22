package com.theah64.xrob.database;

import android.content.Context;

import com.theah64.xrob.models.PendingDelivery;

/**
 * Created by theapache64 on 21/9/16.
 */
public class PendingDeliveries extends BaseTable<PendingDelivery> {

    private static PendingDeliveries instance;

    private PendingDeliveries(Context context) {
        super(context);
    }

    public static PendingDeliveries getInstance(final Context context) {

        if (instance == null) {
            instance = new PendingDeliveries(context);
        }

        return instance;
    }

}
