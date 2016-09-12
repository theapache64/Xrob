package com.theah64.xrob.utils;

import android.app.Application;

import com.theah64.xrob.R;
import com.theah64.xrob.models.Victim;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Created by theapache64 on 11/9/16.
 */
@ReportsCrashes(
        formUri = "https://xrob.cloudant.com/acra-xrob/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        formUriBasicAuthLogin = "yeardstromenewhatheryini",
        formUriBasicAuthPassword = "95f101f733d693b4cdea21f27cfd3c5e34002735",
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        },
        mode = ReportingInteractionMode.SILENT
)
public class Xrob extends Application {

    public static final String KEY_ERROR = "error";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DATA = "data";
    static final String KEY_ERROR_CODE = "error_code";
    public static final String KEY_DATA_TYPE = "data_type";
    public static final String DATA_TYPE_CONTACTS = "contacts";

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
    }
}
