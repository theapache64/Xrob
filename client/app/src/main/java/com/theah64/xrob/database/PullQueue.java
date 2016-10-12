package com.theah64.xrob.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.theah64.xrob.models.PullQueueNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 12/10/16.
 */

public class PullQueue extends BaseTable<PullQueueNode> {

    private static final String COLUMN_FILE_PATH = "file_path";
    private static PullQueue instance;

    private PullQueue(Context context) {
        super(context, "pull_queue");
    }

    public static PullQueue getInstance(Context context) {
        if (instance == null) {
            instance = new PullQueue(context);
        }
        return instance;
    }

    @Override
    public List<PullQueueNode> getAll() {

        List<PullQueueNode> pqs = null;

        final Cursor c = this.getReadableDatabase().query(getTableName(),
                new String[]{COLUMN_ID, COLUMN_FILE_PATH, COLUMN_CREATED_AT},
                COLUMN_IS_BEING_UPLOADED + " = ?",
                new String[]{FALSE},
                null, null, null
        );

        if (c != null && c.moveToFirst()) {

            pqs = new ArrayList<>(c.getCount());

            do {
                final String id = c.getString(c.getColumnIndex(COLUMN_ID));
                final String filePath = c.getString(c.getColumnIndex(COLUMN_FILE_PATH));
                final long capturedAt = c.getLong(c.getColumnIndex(COLUMN_CREATED_AT));

                pqs.add(new PullQueueNode(id, filePath, capturedAt));
            } while (c.moveToNext());

            c.close();
        }

        return pqs;

    }

    @Override
    public long add(PullQueueNode pq) {

        final ContentValues cv = new ContentValues(2);
        cv.put(COLUMN_FILE_PATH, pq.getFilePath());
        cv.put(COLUMN_CREATED_AT, pq.getCapturedAt());

        return this.getWritableDatabase().insert(getTableName(), null, cv);
    }
}
