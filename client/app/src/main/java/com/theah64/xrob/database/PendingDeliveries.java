package com.theah64.xrob.database;

import android.content.Context;
import android.database.Cursor;

import com.theah64.xrob.models.PendingDelivery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 21/9/16.
 */
public class PendingDeliveries extends BaseTable<PendingDelivery> {

    public static final String COLUMN_DATA_TYPE = "data_type";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_IS_ERROR = "is_error";
    private static PendingDeliveries instance;

    private PendingDeliveries(Context context) {
        super(context, "pending_deliveries");
    }

    public static PendingDeliveries getInstance(final Context context) {

        if (instance == null) {
            instance = new PendingDeliveries(context);
        }

        return instance;
    }

    @Override
    public List<PendingDelivery> getAll() {

        List<PendingDelivery> pendingDeliveries = null;
        final Cursor cursor = this.getReadableDatabase().query(
                super.getTableName(),
                new String[]{COLUMN_ID, COLUMN_DATA_TYPE, COLUMN_DATA, COLUMN_MESSAGE, COLUMN_IS_ERROR},
                null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            pendingDeliveries = new ArrayList<>(cursor.getCount());

            do {

                final String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                final boolean isError = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ERROR)) == 1;
                final String dataType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA_TYPE));
                final String data = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA));
                final String message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE));

                pendingDeliveries.add(new PendingDelivery(id, isError, dataType, data, message));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return pendingDeliveries;
    }


}
