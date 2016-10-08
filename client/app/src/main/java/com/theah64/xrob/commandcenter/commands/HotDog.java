package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

import com.theah64.xrob.utils.Pixels;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by theapache64 on 8/10/16.
 * To set url as the wallpaper
 */

public class HotDog extends BaseCommand {

    private static final String FLAG_URL = "u";
    private static final String FLAG_IMAGE = "n";


    private static final Options options = new Options()
            .addOption(FLAG_URL, "URL of the image")
            .addOption(FLAG_IMAGE, "Name of the image");

    HotDog(String command) throws CommandException, ParseException {
        super(command);
    }

    @Override
    public void handle(Context context, Callback callback) {

        String imageUrl = getCmd().getOptionValue(FLAG_URL);

        if (imageUrl == null) {

            //Image url is null, so checking if there's a name.
            final String imageName = getCmd().getOptionValue(FLAG_IMAGE);

            imageUrl = Pixels.getInstance().getImageUrl(imageName);
        }

    }

    @Override
    public Options getOptions() {
        return options;
    }
}
