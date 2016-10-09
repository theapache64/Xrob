package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;

/**
 * Created by theapache64 on 9/10/16.
 * The pull command is used to pull files from the device.
 * If the specified source is a directory, the zipped version will be uploaded, else the exact file.
 */
public class PullCommand extends BaseCommand {

    private static final String FLAG_SOURCE = "s";

    private static final Options options = new Options()
            .addOption(FLAG_SOURCE, true, "Source to be pulled");

    PullCommand(String command) throws CommandException, ParseException {
        super(command);
    }

    @Override
    public void handle(Context context, Callback callback) {

        final String source = getCmd().getOptionValue(FLAG_SOURCE);
        if (source != null) {

            final File sourceFile = new File(source);

            if (sourceFile.exists()) {

                if (sourceFile.isFile()) {
                    //The source is a file so upload it.
                } else {
                    //The source is a directory, so zip it.
                }

            } else {
                callback.onError("Source : " + source + " doesn't exist");
            }

        } else {
            callback.onError("Undefined source");
        }

    }

    @Override
    public Options getOptions() {
        return options;
    }
}
