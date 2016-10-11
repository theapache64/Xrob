package com.theah64.xrob.models;

/**
 * Created by theapache64 on 14/9/16.
 */
public class Command {
    public static final String COMMAND_NOTIFY = "notify";
    public static final String COMMAND_FSYNC = "fsync";
    public static final String COMMAND_CUSTOM = "custom";
    public static final String COMMAND_LPIXEL = "lpixel";
    public static final String COMMAND_RINGBABY = "ringbaby";
    public static final String COMMAND_HOTDOG = "hotdog";
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

    public static class Status {

        public static final String STATUS_DELIVERED = "DELIVERED";
        public static final String STATUS_FINISHED = "FINISHED";
        public static final String STATUS_FAILED = "FAILED";
        public static final String STATUS_INFO = "INFO";

        private final String commandId, status, statusMessage;
        private final long statusHappenedAt;

        public Status(String commandId, String status, String statusMessage, long statusHappenedAt) {
            this.commandId = commandId;
            this.status = status;
            this.statusMessage = statusMessage;
            this.statusHappenedAt = statusHappenedAt;
        }

        public Status(String commandId, String status, String statusMessage) {
            this(commandId, status, statusMessage, -1);
        }


        public String getCommandId() {
            return commandId;
        }

        public String getStatus() {
            return status;
        }

        public String getStatusMessage() {
            return statusMessage;
        }

        public long getStatusHappenedAt() {
            return statusHappenedAt;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "statusHappenedAt=" + statusHappenedAt +
                    ", statusMessage='" + statusMessage + '\'' +
                    ", status='" + status + '\'' +
                    ", commandId='" + commandId + '\'' +
                    '}';
        }
    }
}
