package com.theah64.xrob.commandcenter.commands;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.theah64.xrob.utils.CommonUtils;
import com.theah64.xrob.utils.WallpaperManager;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Locale;


/**
 * Created by theapache64 on 8/10/16.
 */

public class LoremPixelCommand extends BaseCommand {

    private static final String FLAG_CATEGORY = "c";
    private static final String FLAG_HEIGHT = "h";
    private static final String FLAG_WIDTH = "w";
    private static final String FLAG_TEXT = "t";
    private static final String FLAG_GREY = "g";

    private static final int DEFAULT_COUNT_INTERVAL = 30 * 1000; //30 seconds

    private static final String FLAG_LOOP_COUNT = "lc";
    private static final String FLAG_LOOP_INTERVAL = "li";

    private static final String[] VALID_CATEGORIES = {"abstract", "animals", "business", "cats", "city", "food", "nightlife", "fashion", "people", "nature", "sports", "technics", "transport"};

    private static final Options options = new Options()
            .addOption(FLAG_CATEGORY, true, "Photo category")
            .addOption(FLAG_HEIGHT, true, "Height of the image")
            .addOption(FLAG_WIDTH, true, "Width of the image")
            .addOption(FLAG_TEXT, true, "Text on the image")
            .addOption(FLAG_GREY, false, "Is grey image")
            .addOption(FLAG_LOOP_COUNT, true, "Number of images to be changed")
            .addOption(FLAG_LOOP_INTERVAL, true, "Interval of time to change the wallpaper given in the count");

    private static final String X = LoremPixelCommand.class.getSimpleName();

    public LoremPixelCommand(String command) throws CommandException, ParseException {
        super(command);
    }

    private static boolean isValidCategory(String category) {
        if (category != null) {
            for (final String validCategory : VALID_CATEGORIES) {
                if (category.equals(validCategory)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void handle(final Context context, final Callback callback) {

        int width = CommonUtils.parseInt(getCmd().getOptionValue(FLAG_WIDTH));
        int height = CommonUtils.parseInt(getCmd().getOptionValue(FLAG_HEIGHT));

        if (width == -1 || height == -1) {
            //Width or height not given or damaged
            final DisplayMetrics dm = context.getResources().getDisplayMetrics();

            width = dm.widthPixels;
            height = dm.heightPixels;
        }

        String category = getCmd().getOptionValue(FLAG_CATEGORY);

        if (!LoremPixelCommand.isValidCategory(category)) {
            category = null;
        }

        final String text = getCmd().getOptionValue(FLAG_TEXT);
        final boolean isGrey = getCmd().hasOption(FLAG_GREY);

        final String loremImageUrl = new LoremPixelUrlBuilder(width, height, category, text, isGrey).build();

        Log.d(X, "Starting timer configs");

        //has count
        int count = CommonUtils.parseInt(getCmd().getOptionValue(FLAG_LOOP_COUNT));

        Log.d(X, "Count is " + count);

        if (count != -1) {


            Log.d(X, count + " loop wallpaper");

            int interval = CommonUtils.parseInt(getCmd().getOptionValue(FLAG_LOOP_INTERVAL));

            if (interval != -1) {
                interval = interval * 1000;// Converting to milliseconds
            } else {
                interval = DEFAULT_COUNT_INTERVAL;
            }


            final int finalInterval = interval;

            final int totalTime = (count + 1) * interval;//+1 one extra tick


            callback.onInfo(String.format(Locale.getDefault(), "Timer started with count of %d and interval of %d", count, interval));

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    //Starting timer
                    new CountDownTimer(totalTime, finalInterval) {
                        @Override
                        public void onTick(long l) {
                            WallpaperManager.setWallpaper(context, loremImageUrl, callback, true);
                        }

                        @Override
                        public void onFinish() {
                            callback.onSuccess("Timer finished :)");
                        }

                    }.start();
                }
            });

        } else {

            Log.d(X, "Single loop wallpaper");

            //Single time
            WallpaperManager.setWallpaper(context, loremImageUrl, callback, false);
        }


    }

    @Override
    public Options getOptions() {
        return options;
    }

    private static final class LoremPixelUrlBuilder {

        private static final String X = LoremPixelUrlBuilder.class.getSimpleName();
        private final StringBuilder urlBuilder = new StringBuilder("http://lorempixel.com");

        LoremPixelUrlBuilder(final int width, final int height, @Nullable final String category, @Nullable final String textOnTheImage, final boolean isGrey) {

            if (isGrey) {
                urlBuilder.append("/g");
            }

            urlBuilder.append("/").append(width)
                    .append("/").append(height);

            if (category != null) {
                urlBuilder.append("/").append(category);
            }

            if (textOnTheImage != null) {
                urlBuilder.append("/").append(textOnTheImage);
            }

            Log.e(X, "LPixel Url Ready : " + urlBuilder);
        }

        public String build() {
            return urlBuilder.toString();
        }
    }
}
