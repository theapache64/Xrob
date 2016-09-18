package com.theah64.xrob.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theah64.xrob.models.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 18/9/16.
 */
public class CommandStatuses extends BaseTable<Command.Status> {

    public static final String COLUMN_COMMAND_ID = "command_id";
    public static final String COLUMN_STATUS = "status";
    private static final String TABLE_NAME_COMMAND_STATUSES = "command_statuses";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String X = CommandStatuses.class.getSimpleName();
    public static final String COLUMN_STATUS_MESSAGE = "status_message";

    private CommandStatuses(Context context) {
        super(context);
    }

    private static CommandStatuses instance;

    public static CommandStatuses getInstance(final Context context) {

        if (instance == null) {
            instance = new CommandStatuses(context);
        }

        return instance;
    }

    @Override
    public void addv2(Command.Status status) throws RuntimeException {
        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues cv = new ContentValues(4);
        cv.put(COLUMN_COMMAND_ID, status.getCommandId());
        cv.put(COLUMN_STATUS, status.getStatus());
        cv.put(COLUMN_STATUS_MESSAGE, status.getStatusMessage());
        cv.put(COLUMN_CREATED_AT, System.currentTimeMillis());

        db.insert(TABLE_NAME_COMMAND_STATUSES, null, cv);
    }

    @Override
    public List<Command.Status> getAll() {
        List<Command.Status> statusList = null;
        final Cursor cursor = this.getReadableDatabase().query(TABLE_NAME_COMMAND_STATUSES, new String[]{COLUMN_COMMAND_ID, COLUMN_STATUS, COLUMN_STATUS_MESSAGE, COLUMN_CREATED_AT}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            statusList = new ArrayList<>();
            do {
                final String commandId = cursor.getString(cursor.getColumnIndex(COLUMN_COMMAND_ID));
                final String status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
                final String statusMessage = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS_MESSAGE));
                final long happenedAt = cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED_AT));

                statusList.add(new Command.Status(commandId, status, statusMessage, happenedAt));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return statusList;
    }

    @Override
    public void deleteAll() {
        Log.i(X, "Deleting all command statuses");
        super.deleteAll(TABLE_NAME_COMMAND_STATUSES);
    }

}
