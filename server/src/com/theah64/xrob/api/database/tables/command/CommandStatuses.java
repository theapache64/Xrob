package com.theah64.xrob.api.database.tables.command;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.database.tables.BaseTable;
import com.theah64.xrob.api.models.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by theapache64 on 15/9/16,1:36 AM.
 */
public class CommandStatuses extends BaseTable<Command.Status> {

    public static final String COLUMN_COMMAND_ID = "command_id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_STATUS_MESSAGE = "status_message";

    private CommandStatuses() {
    }

    private static final CommandStatuses instance = new CommandStatuses();

    public static CommandStatuses getInstance() {
        return instance;
    }

    @Override
    public boolean add(Command.Status status) {
        boolean isAdded = false;
        final String addClientQuery = "INSERT INTO command_statuses (command_id,status,status_message) VALUES (?,?,?);";
        final java.sql.Connection connection = Connection.getConnection();

        //To track the success

        try {
            final PreparedStatement ps = connection.prepareStatement(addClientQuery);

            ps.setString(1, status.getCommandId());
            ps.setString(2, status.getStatus());
            ps.setString(3, status.getStatusMessage());

            isAdded = ps.executeUpdate() == 1;

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isAdded;
    }
}
