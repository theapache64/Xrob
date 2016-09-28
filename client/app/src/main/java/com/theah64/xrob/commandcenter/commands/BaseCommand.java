package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by theapache64 on 14/9/16,7:48 PM.
 */
public abstract class BaseCommand {

    private static final java.lang.String BASE_COMMAND_CODE = "xrob";
    private final String[] args;
    private CommandLine cmd;

    public BaseCommand(String command) throws CommandException, ParseException {
        if (command != null && !command.isEmpty()) {
            if (command.startsWith(BASE_COMMAND_CODE)) {
                this.args = command.split(" ");
                if (args.length < 3) {
                    throw new IllegalArgumentException("Command should have at least 3 parts");
                }else{
                    //Valid command syntax, check for options
                    if(getOptions()!=null){
                        final CommandLineParser parser = new DefaultParser();
                        this.cmd = parser.parse(getOptions(),args);
                    }
                }
            } else {
                throw new CommandException("Every command should start with " + BASE_COMMAND_CODE);
            }
        } else {
            throw new CommandException("Command can't empty!");
        }
    }

    public String getCommandType() {
        return args[1];
    }

    public CommandLine getCmd() {
        return cmd;
    }

    public static class CommandException extends Exception {
        public CommandException(String s) {
            super(s);
        }
    }

    public abstract void handle(final Context context, final Callback callback);

    public interface Callback {
        void onError(final String message);

        void onSuccess(final String message);
    }

    public abstract Options getOptions();

}
