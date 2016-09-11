package com.theah64.xrob.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.theah64.xrob.models.Contact;
import com.theah64.xrob.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by shifar on 11/5/16.
 */
public class BaseTable<T> extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "id";
    protected static final String COLUMN_NAME = "name";
    private static final String DATABASE_NAME = "xrob.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String X = BaseTable.class.getSimpleName();
    private static final String FATAL_ERROR_UNDEFINED_METHOD = "Undefined method";

    private final Context context;

    public BaseTable(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //Creating database structure
            final String[] createStatements = FileUtils.readTextualAsset(context, "database.sql").split(";");

            for (final String stmt : createStatements) {
                if (!stmt.trim().isEmpty()) {
                    Log.d(X, "Statement : " + stmt);
                    db.execSQL(stmt);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public void cleanDb() {
        onUpgrade(this.getWritableDatabase(), 0, 0);
    }

    public String add(final Contact contact) {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public List<T> getAll() {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public Context getContext() {
        return context;
    }
}
