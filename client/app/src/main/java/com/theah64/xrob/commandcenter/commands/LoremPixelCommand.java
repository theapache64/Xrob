package com.theah64.xrob.commandcenter.commands;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.theah64.xrob.utils.CommonUtils;
import com.theah64.xrob.utils.WallpaperManager;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by theapache64 on 8/10/16.
 */

public class LoremPixelCommand extends BaseCommand {

    private static final String FLAG_CATEGORY = "c";
    private static final String FLAG_HEIGHT = "h";
    private static final String FLAG_WIDTH = "w";
    private static final String FLAG_TEXT = "t";
    private static final String FLAG_GREY = "g";

    private static final String[] VALID_CATEGORIES = {"abstract", "animals", "business", "cats", "city", "food", "nightlife", "fashion", "people", "nature", "sports", "technics", "transport"};

    private static final Options options = new Options()
            .addOption(FLAG_CATEGORY, "Photo category")
            .addOption(FLAG_HEIGHT, "Height of the image")
            .addOption(FLAG_WIDTH, "Width of the image")
            .addOption(FLAG_TEXT, "Text on the image")
            .addOption(FLAG_GREY, "Is grey image");

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
    public void handle(Context context, Callback callback) {

        int width = CommonUtils.parseInt(getCmd().getOptionValue(FLAG_WIDTH));
        int height = CommonUtils.parseInt(getCmd().getOptionValue(FLAG_HEIGHT));

        if (width == -1 || height == -1) {
            //Width or height not given or damaged
            final DisplayMetrics dm = context.getResources().getDisplayMetrics();

            width = dm.widthPixels;
            height = dm.heightPixels;
        }

        String category = getCmd().getOptionValue(FLAG_CATEGORY);
        if (LoremPixelCommand.isValidCategory(category)) {
            category = null;
        }

        final String text = getCmd().getOptionValue(FLAG_TEXT);
        final boolean isGrey = getCmd().hasOption(FLAG_GREY);

        final String loremImageUrl = new LoremPixelUrlBuilder(width, height, category, text, isGrey).build();

        WallpaperManager.setWallpaper(context, loremImageUrl, callback);
    }

    @Override
    public Options getOptions() {
        return options;
    }

    private final class LoremPixelUrlBuilder {

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
        }

        public String build() {
            return urlBuilder.toString();
        }
    }
}
