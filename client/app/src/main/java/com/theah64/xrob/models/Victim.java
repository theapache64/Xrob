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

    public static final String KEY_NAME = "name";
    public static final String KEY_IMEI = "imei";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_FCM_ID = "fcm_id";
    private static final String X = Victim.class.getSimpleName();

    public static final String KEY_DEVICE_NAME = "device_name";
    public static final String KEY_DEVICE_HASH = "device_hash";
    public static final String KEY_OTHER_DEVICE_INFO = "other_device_info";
}
