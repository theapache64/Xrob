package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by theapache64 on 8/10/16.
 */

public class ADBCommand extends BaseCommand {

    ADBCommand(String command) throws CommandException, ParseException {
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
