package com.theah64.xrob.commandcenter.commands;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.theah64.xrob.asynctasks.NotificationPopper;

import org.apache.commons.cli.*;

/**
 * Created by theapache64 on 14/9/16,7:52 PM.
 */
public class NotificationCommand extends BaseCommand {

    private static final String OPTION_TITLE = "t";
    private static final String OPTION_TICKER = "k";
    private static final String OPTION_CONTENT = "c";
    private static final String OPTION_IMAGE_URL = "i";
    private static final String OPTION_CONTENT_URL = "u";

    private static final Options notifyCmdOption = new Options()
            .addOption(new Option(OPTION_TITLE, true, "Notification's title"))
            .addOption(new Option(OPTION_TICKER, true, "Notification's ticket text"))
            .addOption(new Option(OPTION_CONTENT, true, "Notification's content text"))
            .addOption(new Option(OPTION_IMAGE_URL, true, "Notification's Big image url"))
            .addOption(new Option(OPTION_CONTENT_URL, true, "URL to open on notification click"));

    private String title, ticker, content, imageUrl, contentUrl;

    public NotificationCommand(final String command) throws BaseCommand.CommandException {

        super(command);
        final CommandLineParser parser = new DefaultParser();
        try {
            final CommandLine cmd = parser.parse(notifyCmdOption, getArgs());
            this.title = cmd.getOptionValue(OPTION_TITLE);
            this.ticker = cmd.getOptionValue(OPTION_TICKER);
            this.content = cmd.getOptionValue(OPTION_CONTENT);
            this.imageUrl = cmd.getOptionValue(OPTION_IMAGE_URL);
            this.contentUrl = cmd.getOptionValue(OPTION_CONTENT_URL);


        } catch (ParseException e) {
            e.printStackTrace();
            throw new BaseCommand.CommandException("Invalid command: " + e.getMessage());
        }
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    @Override
    public void handle(Context context, Callback callback) {
        new NotificationPopper(context, this, callback).execute();
    }

    public String getTicker() {
        return ticker;
    }
}
