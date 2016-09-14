package com.theah64.xrob.api.models;

import com.theah64.xrob.api.database.tables.Commands;
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

    private final String id, command, relativeEstablishedTime;
    private final List<Status> statuses;

    public Command(String id, String command, long reportedAt, List<Status> statuses) {
        this.id = id;
        this.command = command;
        this.relativeEstablishedTime = TimeUtils.getRelativeTime(reportedAt);
        this.statuses = statuses;
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

    public static class Status {

        private final String status, statusMessage, relativeReportTime;

        public Status(String status, String statusMessage, long reportedAt) {
            this.status = status;
            this.statusMessage = statusMessage;
            this.relativeReportTime = TimeUtils.getRelativeTime(reportedAt);
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
