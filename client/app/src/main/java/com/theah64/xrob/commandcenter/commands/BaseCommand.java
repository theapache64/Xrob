package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

/**
 * Created by theapache64 on 14/9/16,7:48 PM.
 */
public abstract class BaseCommand {

    private static final java.lang.String BASE_COMMAND_CODE = "xrob";
    private final String[] args;

    public BaseCommand(String command) throws CommandException {
        if (command != null && !command.isEmpty()) {
            if (command.startsWith(BASE_COMMAND_CODE)) {
                this.args = command.split(" ");
                if (args.length < 3) {
                    throw new IllegalArgumentException("Command should have at least 3 parts");
                }
            } else {
                throw new CommandException("Every command should start with " + BASE_COMMAND_CODE);
            }
        } else {
            throw new CommandException("Command can't empty!");
        }
    }

    public String[] getArgs() {
        return args;
    }

    public String getCommandType() {
        return args[1];
    }

    public static class CommandException extends Exception {
        public CommandException(String s) {
            super(s);
        }
    }

    public abstract void handle(final Context context,final Callback callback);

    public interface Callback {
        void onError(final String message);

        void onSuccess(final String message);
    }
}
