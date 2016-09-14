package com.theah64.xrob.models;

/**
 * Created by theapache64 on 14/9/16.
 */
public class Command {
    public static final String COMMAND_NOTIFY = "notify";
    private final String id, command;

    public Command(String id, String command) {
        this.id = id;
        this.command = command;
    }

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public static class CommandException extends Exception {
        public CommandException(String s) {
            super(s);
        }
    }
}
