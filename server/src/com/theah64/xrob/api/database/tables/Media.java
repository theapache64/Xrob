package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.MediaNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 9/10/16.
 */
public class Media extends BaseTable<MediaNode> {

    private static final Media instance = new Media();
    private static final String COLUMN_TYPE = "_type";
    public static final String COLUMN_DOWNLOAD_LINK = "download_link";
    private static final String COLUMN_FILE_SIZE_IN_KB = "file_size_in_kb";
    public static final String COLUMN_CAPTURED_AT = "captured_at";

    private Media() {
        super("media");
    }

    public static Media getInstance() {
        return instance;
    }

    @Override
    public void addv2(@Nullable String victimId, JSONArray jaMedia) throws RuntimeException, JSONException, SQLException {

        //Getting media length.
        final int mediaLength = jaMedia.length();

        if (mediaLength > 0) {

            final String query = "INSERT INTO media (victim_id, name, _type, download_link, file_size_in_kb, captured_at) VALUES (?,?,?,?,?,?);";
            final java.sql.Connection con = Connection.getConnection();

            final PreparedStatement ps = con.prepareStatement(query);

            for (int i = 0; i < mediaLength; i++) {

                final JSONObject joMedia = jaMedia.getJSONObject(i);

                final String type = joMedia.getString(COLUMN_TYPE);

                if (MediaNode.isValidType(type)) {

                    final String name = joMedia.getString(COLUMN_NAME);
                    final String downloadLink = joMedia.getString(COLUMN_DOWNLOAD_LINK);
                    final int fileSizeInKb = joMedia.getInt(COLUMN_FILE_SIZE_IN_KB);
                    final long capturedAt = joMedia.getLong(COLUMN_CAPTURED_AT);

                    ps.setString(1, victimId);
                    ps.setString(2, name);
                    ps.setString(3, type);
                    ps.setString(4, downloadLink);
                    ps.setInt(5, fileSizeInKb);
                    ps.setLong(6, capturedAt);

                    final boolean isAdded = ps.executeUpdate() == 1;

                    if (!isAdded) {
                        throw new IllegalArgumentException("Failed to add media node " + name);
                    }

                } else {
                    throw new IllegalArgumentException("Invalid media type : " + type);
                }

            }

            ps.close();
            con.close();

        } else {
            throw new IllegalArgumentException("Media is empty!");
        }

    }


}
