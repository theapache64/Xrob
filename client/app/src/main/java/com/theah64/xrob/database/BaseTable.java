package com.theah64.xrob.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
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

    static final String FALSE = "0";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    private static final String DATABASE_NAME = "xrob.db";

    protected static final String COLUMN_CREATED_AT = "created_at";
    private static final int DATABASE_VERSION = 1;
    private static final String X = BaseTable.class.getSimpleName();
    private static final String FATAL_ERROR_UNDEFINED_METHOD = "Undefined method";

    private final Context context;
    private final String tableName;

    public BaseTable(final Context context, String tableName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.tableName = tableName;
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

            db.execSQL("CREATE TRIGGER after_phone_numbers_insert AFTER INSERT ON phone_numbers BEGIN UPDATE contacts SET is_synced = 0 WHERE is_synced = 1 AND id = NEW.contact_id; END;");

            //Adding all messages to the messages table
            syncMessages();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /*
    SAMPLE - 
    09-29 23:34:26.440 16184-16669/com.theah64.xrob D/BaseTable: ---------------------------
    09-29 23:34:26.440 16184-16669/com.theah64.xrob D/BaseTable: _id = 747
    09-29 23:34:26.441 16184-16669/com.theah64.xrob D/BaseTable: thread_id = 6
    09-29 23:34:26.442 16184-16669/com.theah64.xrob D/BaseTable: address = AL-650001
    09-29 23:34:26.443 16184-16669/com.theah64.xrob D/BaseTable: m_size = null
    09-29 23:34:26.444 16184-16669/com.theah64.xrob D/BaseTable: person = null
    09-29 23:34:26.444 16184-16669/com.theah64.xrob D/BaseTable: date = 1460015993000
    09-29 23:34:26.445 16184-16669/com.theah64.xrob D/BaseTable: date_sent = 0
    09-29 23:34:26.446 16184-16669/com.theah64.xrob D/BaseTable: protocol = 0
    09-29 23:34:26.448 16184-16669/com.theah64.xrob D/BaseTable: read = 1
    09-29 23:34:26.448 16184-16669/com.theah64.xrob D/BaseTable: status = -1
    09-29 23:34:26.449 16184-16669/com.theah64.xrob D/BaseTable: type = 1
    09-29 23:34:26.449 16184-16669/com.theah64.xrob D/BaseTable: reply_path_present = 0
    09-29 23:34:26.450 16184-16669/com.theah64.xrob D/BaseTable: subject = null
    09-29 23:34:26.450 16184-16669/com.theah64.xrob D/BaseTable: body = Rs11=Loc mob30p/m(Per day 1st min @Re1)28days
                                                                 Rs39=Loc Airtel mob15p/m(Per day 1st min @Re1)28days
                                                                 Dial *121*1# or visit Airtel retailer.Offer Valid Today!
    09-29 23:34:26.451 16184-16669/com.theah64.xrob D/BaseTable: service_center = +919840011010
    09-29 23:34:26.452 16184-16669/com.theah64.xrob D/BaseTable: locked = 0
    09-29 23:34:26.453 16184-16669/com.theah64.xrob D/BaseTable: sub_id = -1
    09-29 23:34:26.453 16184-16669/com.theah64.xrob D/BaseTable: error_code = 0
    09-29 23:34:26.454 16184-16669/com.theah64.xrob D/BaseTable: creator = null
    09-29 23:34:26.454 16184-16669/com.theah64.xrob D/BaseTable: seen = 1
    09-29 23:34:26.455 16184-16669/com.theah64.xrob D/BaseTable: itemInfoid = 747
    09-29 23:34:26.455 16184-16669/com.theah64.xrob D/BaseTable: receive_date = 1460015962217
    09-29 23:34:26.456 16184-16669/com.theah64.xrob D/BaseTable: ipmsg_id = 0
    09-29 23:34:26.456 16184-16669/com.theah64.xrob D/BaseTable: ref_id = null
    09-29 23:34:26.457 16184-16669/com.theah64.xrob D/BaseTable: total_len = null
    09-29 23:34:26.457 16184-16669/com.theah64.xrob D/BaseTable: rec_len = null
    09-29 23:34:26.458 16184-16669/com.theah64.xrob D/BaseTable: ---------------------------

     */
    private void syncMessages() {
        Log.d(X, "Syncing messages");

        final String[] smsTypes = {"inbox", "sent", "draft"};

        for (final String smsType : smsTypes) {
            final Uri uri = Uri.parse(String.format("content://sms/%s", smsType));

            final Cursor c = getContext().getContentResolver().query(uri, null, null, null, null);

            if (c != null) {

                if (c.moveToFirst()) {

                    do {
                        Log.d(X, "---------------------------");
                        for (int index = 0; index < c.getColumnCount(); index++) {
                            Log.d(X, c.getColumnName(index) + " = " + c.getString(index));
                        }
                    } while (c.moveToNext());

                }

                c.close();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS phone_numbers");
        db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL("DROP TABLE IF EXISTS command_statuses");
        db.execSQL("DROP TABLE IF EXISTS pending_deliveries");
        //TODO: Remove all tables here
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

    public String get(final String whereColumn, final String whereColumnValue, final String columnToReturn) {

        String valueToReturn = null;

        final Cursor cur = this.getWritableDatabase().query(getTableName(), new String[]{columnToReturn}, whereColumn + " = ? ", new String[]{whereColumnValue}, null, null, null, "1");

        if (cur.moveToFirst()) {
            valueToReturn = cur.getString(cur.getColumnIndex(columnToReturn));
        }

        cur.close();

        return valueToReturn;
    }


    protected boolean update(String whereColumn, String whereColumnValue, String columnToUpdate, String valueToUpdate) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues cv = new ContentValues(1);
        cv.put(columnToUpdate, valueToUpdate);
        return db.update(tableName, cv, whereColumn + " = ? ", new String[]{whereColumnValue}) > 0;
    }


    public T get(final String column1, final String value1, final String column2, final String value2) {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public List<T> getAll() {
        throw new IllegalArgumentException(FATAL_ERROR_UNDEFINED_METHOD);
    }

    public void deleteAll() {
        this.getWritableDatabase().delete(getTableName(), null, null);
    }

    public final boolean delete(final String whereColumn, final String whereColumnValue) {
        return this.getReadableDatabase().delete(tableName, whereColumn + " = ?", new String[]{whereColumnValue}) == 1;
    }


    public Context getContext() {
        return context;
    }

    public final String getTableName() {
        return tableName;
    }
}
