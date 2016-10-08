package com.theah64.xrob.commandcenter.commands;

import android.content.Context;
import android.util.Log;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by theapache64 on 8/10/16.
 */

public class CustomCommand extends BaseCommand {

    private static final String X = CustomCommand.class.getSimpleName();
    private final String adbCommand;

    public CustomCommand(String command) throws CommandException, ParseException {
        super(command);
        this.adbCommand = command.replaceAll("xrob custom ", "");
    }

    @Override
    public void handle(Context context, Callback callback) {

        Log.d(X, "Executing custom command : " + adbCommand);
        try {
            final java.lang.Process p = Runtime.getRuntime().exec(adbCommand);
            final BufferedReader brStd = new BufferedReader(new InputStreamReader(p.getInputStream()));
            final BufferedReader brStdErr = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            final StringBuilder sb = new StringBuilder();
            while ((line = brStd.readLine()) != null) {
                sb.append(line).append("\n");
            }
                
            while ((line = brStdErr.readLine()) != null) {
                sb.append(line).append("\n");
            }

            callback.onSuccess("Response: " + sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }

    }

    @Override
    public Options getOptions() {
        return null;
    }
}
