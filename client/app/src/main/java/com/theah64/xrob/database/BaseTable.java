package com.theah64.xrob.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
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
    public static final String COLUMN_NAME = "name";
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
                    db.execSQL(stmt + ";");
                }
            }

            db.execSQL("CREATE TRIGGER after_contacts_update AFTER UPDATE ON contacts FOR EACH ROW BEGIN UPDATE contacts SET is_synced = 0 WHERE id = OLD.id; END;");
            db.execSQL("CREATE TRIGGER after_phone_numbers_update AFTER UPDATE ON phone_numbers FOR EACH ROW BEGIN UPDATE phone_numbers SET is_synced = 0 WHERE id = OLD.id; END;");

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

    public T get(final String column, final String value) {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public boolean add(@Nullable final String userId, final JSONObject jsonObject) {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }


    public long add(T newInstance) {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public void addv2(T newInstance) throws RuntimeException {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public String get(final String byColumn, final String byValues, final String columnToReturn) {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public boolean update(String whereColumn, String whereColumnValue, String updateColumn, String newUpdateColumnValue) {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }


    protected boolean update(String tableName, String whereColumn, String whereColumnValue, String columnToUpdate, String valueToUpdate) {
        boolean isEdited = false;
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues cv = new ContentValues(1);
        cv.put(columnToUpdate, valueToUpdate);
        isEdited = db.update(tableName, cv, whereColumn + " = ? ", new String[]{whereColumnValue}) > 0;
        db.close();
        return isEdited;
    }

    public T get(final String column1, final String value1, final String column2, final String value2) {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public List<T> getAll() {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public Context getContext() {
        return context;
    }
}
