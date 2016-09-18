package com.theah64.xrob.api.database.tables.command;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.database.tables.BaseTable;
import com.theah64.xrob.api.models.Command;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by theapache64 on 15/9/16,1:36 AM.
 */
public class CommandStatuses extends BaseTable<Command.Status> {

    public static final String COLUMN_COMMAND_ID = "command_id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_STATUS_MESSAGE = "status_message";
    private static final String TABLE_NAME_COMMAND_STATUSES = "command_statuses";
    public static final String COLUMN_STATUS_HAPPENED_AT = "status_happened_at";

    private CommandStatuses() {
    }

    private static final CommandStatuses instance = new CommandStatuses();

    public static CommandStatuses getInstance() {
        return instance;
    }

    @Override
    public boolean add(Command.Status status) {
        boolean isAdded = false;
        final String addClientQuery = "INSERT INTO command_statuses (command_id,status,status_message,status_happened_at) VALUES (?,?,?,?);";
        final java.sql.Connection con = Connection.getConnection();

        //To track the success

        try {
            final PreparedStatement ps = con.prepareStatement(addClientQuery);

            ps.setString(1, status.getCommandId());
            ps.setString(2, status.getStatus());
            ps.setString(3, status.getStatusMessage());
            ps.setLong(4, status.getCommandHappenedAt());

            System.out.println("Command happened at " + status.getCommandHappenedAt());

            isAdded = ps.executeUpdate() == 1;

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
        return isAdded;
    }

    @Override
    public boolean isExist(String whereColumn1, String whereColumnValue1, String whereColumn2, String whereColumnValue2) {
        return super.isExist(TABLE_NAME_COMMAND_STATUSES, whereColumn1, whereColumnValue1, whereColumn2, whereColumnValue2);
    }
}
