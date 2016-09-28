package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by theapache64 on 28/9/16.
 */

public class FCMUpdateCommand extends BaseCommand {

    public FCMUpdateCommand(String command) throws CommandException, ParseException {
        super(command);
    }

    @Override
    public void handle(Context context, Callback callback) {
        
    }

    @Override
    public Options getOptions() {
        return null;
    }
}
