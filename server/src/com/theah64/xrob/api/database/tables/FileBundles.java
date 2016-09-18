package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.FileBundle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 18/9/16,4:36 PM.
 */
public class FileBundles extends BaseTable<FileBundle> {
    private FileBundles() {
    }

    private static final FileBundles instance = new FileBundles();

    public static FileBundles getInstance() {
        return instance;
    }


    @Override
    public String addv3(FileBundle fileBundle) {
        String bundleId = null;
        final String addBundleQuery = "INSERT INTO file_bundles (victim_id) VALUES (?);";
        final java.sql.Connection con = Connection.getConnection();

        //To track the success

        try {
            final PreparedStatement ps = con.prepareStatement(addBundleQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, fileBundle.getVictimId());

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
}
