package com.theah64.xrob.api.database.tables.command;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.database.tables.BaseTable;
import com.theah64.xrob.api.models.Command;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by theapache64 on 15/9/16,1:36 AM.
 */
public class CommandStatuses extends BaseTable<Command.Status> {

    public static final String COLUMN_COMMAND_ID = "command_id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_STATUS_MESSAGE = "status_message";
    public static final String COLUMN_STATUS_HAPPENED_AT = "status_happened_at";

    private CommandStatuses() {
        super("command_statuses");
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
    public void addv2(@Nullable String victimId, JSONArray jaCommandStatuses) throws RuntimeException, JSONException {

        final CommandStatuses cStatuesTable = CommandStatuses.getInstance();

        for (int i = 0; i < jaCommandStatuses.length(); i++) {

            final JSONObject joCommandStatus = jaCommandStatuses.getJSONObject(i);

            final String commandId = joCommandStatus.getString(CommandStatuses.COLUMN_COMMAND_ID);

            //is command exist
            final boolean isCommandExistAndEstablishedForThisVictim = Commands.getInstance().isExist(Commands.COLUMN_ID, commandId, Commands.COLUMN_VICTIM_ID, victimId);

            if (isCommandExistAndEstablishedForThisVictim) {

                final String status = joCommandStatus.getString(CommandStatuses.COLUMN_STATUS);

                if (Command.Status.isValid(status)) {

                    //info status can be multiple.
                    final boolean isStatusAlreadyExists = cStatuesTable.isExist(CommandStatuses.COLUMN_COMMAND_ID, commandId, CommandStatuses.COLUMN_STATUS, status) && !status.equals(Command.Status.STATUS_INFO);

                    final long commandProcessedAt = joCommandStatus.getLong(CommandStatuses.COLUMN_STATUS_HAPPENED_AT);

                    final boolean isStatusAdded = isStatusAlreadyExists || cStatuesTable.add(new Command.Status(
                            status,
                            joCommandStatus.getString(CommandStatuses.COLUMN_STATUS_MESSAGE),
                            0,//now
                            commandProcessedAt, commandId
                    ));

                    if (!isStatusAdded) {
                        throw new IllegalArgumentException("Failed to add command status");
                    }

                } else {
                    throw new IllegalArgumentException("Invalid status : " + status);
                }


            } else {
                throw new IllegalArgumentException("Command doesn't exist or not established for you");
            }
        }

    }

}
