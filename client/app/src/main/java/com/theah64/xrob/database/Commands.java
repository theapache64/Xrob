package com.theah64.xrob.database;

import android.content.Context;

import com.theah64.xrob.models.Command;

/**
 * Created by theapache64 on 14/9/16.
 */
public class Commands extends BaseTable<Command> {

    public static final String COLUMN_COMMAND = "command";
    private static Commands instance;

    public static Commands getInstance(final Context context) {

        if (instance == null) {
            instance = new Commands(context);
        }

        return instance;
    }

    private Commands(Context context) {
        super(context);
    }
}
