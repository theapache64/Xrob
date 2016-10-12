package com.theah64.xrob.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.theah64.xrob.models.PendingDelivery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 21/9/16.
 */
public class PendingDeliveries extends BaseTable<PendingDelivery> {

    private static final String COLUMN_DATA_TYPE = "data_type";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_IS_ERROR = "is_error";
    private static final String X = PendingDeliveries.class.getSimpleName();
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
    public long add(PendingDelivery pd) {

        Log.d(X, "PendingDeliveries: " + pd);

        final ContentValues cv = new ContentValues(4);

        cv.put(COLUMN_IS_ERROR, pd.isError());
        cv.put(COLUMN_DATA_TYPE, pd.getDataType());
        cv.put(COLUMN_DATA, pd.getData());
        cv.put(COLUMN_MESSAGE, pd.getMessage());
        cv.put(COLUMN_CREATED_AT, System.currentTimeMillis());

        final long newRowId = this.getWritableDatabase().insert(getTableName(), null, cv);
        if (newRowId != -1) {
            Log.d(X, "Added : " + newRowId);
        } else {
            throw new IllegalArgumentException("Failed to add pending delivery");
        }

        return newRowId;
    }

    @Override
    public List<PendingDelivery> getAll() {

        List<PendingDelivery> pendingDeliveries = null;

        //Retrvng real pending deliveries
        final Cursor cursor = this.getReadableDatabase().query(
                super.getTableName(),
                new String[]{COLUMN_ID, COLUMN_DATA_TYPE, COLUMN_DATA, COLUMN_MESSAGE, COLUMN_IS_ERROR},
                COLUMN_IS_BEING_UPLOADED + " = ?", new String[]{"0"}, null, null, null
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
