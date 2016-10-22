package com.theah64.xrob.commandcenter.commands;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.theah64.xrob.utils.CommonUtils;
import com.theah64.xrob.utils.WallpaperManager;
import com.theah64.xrob.utils.gpix.Callback;
import com.theah64.xrob.utils.gpix.GPix;
import com.theah64.xrob.utils.gpix.Image;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;


/**
 * Created by theapache64 on 20/10/16.
 */

public class GPixCommand extends BaseCommand {

    private static final String FLAG_KEYWORD = "k";
    private static final String FLAG_NUMBER_OF_IMAGES = "n";
    private static final String FLAG_INTERVAL = "i";
    private static final String FLAG_THUMB = "t";

    private static final String FLAG_ORIGINAL = "o";
    private static final Options options = new Options()
            .addOption(FLAG_KEYWORD, true, "Keyword")
            .addOption(FLAG_NUMBER_OF_IMAGES, true, "Number of images")
            .addOption(FLAG_INTERVAL, true, "Loop interval in seconds")
            .addOption(FLAG_THUMB, false, "Thumbnail image")
            .addOption(FLAG_ORIGINAL, false, "Original image");

    private static final String DEFAULT_NUMBER_OF_IMAGES = "1";
    private static final long DEFAULT_INTERVAL_IN_SECONDS = 5;

    private static final int FLAG_VALUE_THUMBNAIL = 0;
    private static final int FLAG_VALUE_ORIGINAL_IMAGE = 1;

    private static final String X = GPixCommand.class.getSimpleName();
    private int imagesChanged;

    public GPixCommand(String command) throws CommandException, ParseException {
        super(command);
        imagesChanged = 0;
    }

    @Override
    public void handle(final Context context, final Callback callback) {

        final String keyword = getCmd().getOptionValue(FLAG_KEYWORD);

        if (keyword != null) {

            final int numberOfImages = CommonUtils.parseInt(getCmd().getOptionValue(FLAG_NUMBER_OF_IMAGES, DEFAULT_NUMBER_OF_IMAGES));

            try {
                GPix.getInstance().search(keyword, numberOfImages, new com.theah64.xrob.utils.gpix.Callback() {
                    @Override
                    public void onResult(@NotNull final List<Image> imageList) {

                        final long loopInterval = CommonUtils.parseLong(getCmd().getOptionValue(FLAG_INTERVAL), DEFAULT_INTERVAL_IN_SECONDS) * 1000;

                        final int imageFlag;
                        if (getCmd().hasOption(FLAG_ORIGINAL)) {
                            imageFlag = FLAG_VALUE_ORIGINAL_IMAGE;
                        } else {
                            imageFlag = FLAG_VALUE_THUMBNAIL;
                        }


                        final long totalTime = loopInterval * (imageList.size() + 1);

                        callback.onInfo(imageList.size() + " images are ready to set as wallaper with interval of " + loopInterval + "ms | total time : " + totalTime + "ms");

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {

                                new CountDownTimer(totalTime, loopInterval) {

                                    @Override
                                    public void onTick(long l) {

                                        final Image image = imageList.get(imagesChanged++);

                                        Log.d(X, "Current image : " + image);

                                        WallpaperManager.setWallpaper(context, imageFlag == FLAG_VALUE_ORIGINAL_IMAGE ? image.getImageUrl() : image.getThumbImageUrl(),
                                                callback, imageList.size() > 1);

                                    }

                                    @Override
                                    public void onFinish() {
                                        callback.onSuccess(imagesChanged + " time(s) wallpaper changed.");
                                    }

                                }.start();
                            }
                        });


                    }

                    @Override
                    public void onError(String reason) {
                        callback.onError(reason);
                    }
                });
            } catch (GPix.GPixException | IOException | JSONException e) {
                e.printStackTrace();
                callback.onError(e.getMessage());
            }


        } else {
            callback.onError("Keyword missing");
        }
    }

    @Override
    public Options getOptions() {
        return options;
    }
}
