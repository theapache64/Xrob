package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

/**
 * Created by theapache64 on 22/9/16.
 */
public class ContactsSyncCommand extends BaseCommand {

    public ContactsSyncCommand(String command) throws CommandException {
        super(command);
    }

    @Override
    public void handle(Context context, Callback callback) {
        
    }
}
