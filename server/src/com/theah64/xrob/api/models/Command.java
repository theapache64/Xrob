package com.theah64.xrob.api.models;

import com.theah64.xrob.api.database.tables.command.Commands;
import com.theah64.xrob.api.utils.FCMUtils;
import com.theah64.xrob.api.utils.clientpanel.TimeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by theapache64 on 14/9/16,4:37 PM.
 */
public class Command {

    public static final String COMMAND_NOTIFY = "notify";
    public static final String REGEX_VALID_COMMAND = "^xrob\\s(?:notify).+";

    private String id;
    private final String command;
    private final String relativeEstablishedTime;
    private final String victimId;
    private final String clientId;
    private final List<Status> statuses;

    public Command(String id, String command, long reportedAt, List<Status> statuses, String victimId, String clientId) {
        this.id = id;
        this.command = command;
        this.relativeEstablishedTime = TimeUtils.getRelativeTime(reportedAt);
        this.statuses = statuses;
        this.victimId = victimId;
        this.clientId = victimId;
    }

    public String getCommand() {
        return command;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    @Override
    public String toString() {
        return "Command{" +
                "id='" + id + '\'' +
                ", command='" + command + '\'' +
                ", relativeEstablishedTime='" + relativeEstablishedTime + '\'' +
                ", statuses=" + statuses +
                '}';
    }

    public String getRelativeEstablishedTime() {
        return relativeEstablishedTime;
    }

    public static String toFcmPayload(final String fcmId, Command command) {

        final JSONObject joFcm = new JSONObject();
        try {

            joFcm.put(FCMUtils.KEY_TO, fcmId);

            final JSONObject joData = new JSONObject();

            final JSONObject joCommand = new JSONObject();
            joCommand.put(Commands.COLUMN_COMMAND, command.getCommand());
            joCommand.put(Commands.COLUMN_ID, command.getId());

            joData.put(FCMUtils.KEY_TYPE, FCMUtils.TYPE_COMMAND);
            joData.put(FCMUtils.KEY_TYPE_DATA, joCommand);

            joFcm.put(FCMUtils.KEY_DATA, joData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return joFcm.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getVictimId() {
        return victimId;
    }

    public static class Status {

        private final String status, statusMessage, relativeReportTime,commandId;

        public Status(String status, String statusMessage, long reportedAt, String commandId) {
            this.status = status;
            this.statusMessage = statusMessage;
            this.commandId = commandId;
            this.relativeReportTime = TimeUtils.getRelativeTime(reportedAt);
        }

        public String getStatus() {
            return status;
        }

        public String getStatusMessage() {
            return statusMessage;
        }

        public String getRelativeReportTime() {
            return relativeReportTime;
        }

        public String getCommandId() {
            return commandId;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "status='" + status + '\'' +
                    ", statusMessage='" + statusMessage + '\'' +
                    ", relativeReportTime='" + relativeReportTime + '\'' +
                    '}';
        }
    }
}
