package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

import com.theah64.xrob.asynctasks.NotificationPopper;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by theapache64 on 14/9/16,7:52 PM.
 */
public class NotificationCommand extends BaseCommand {

    private static final String FLAG_TITLE = "t";
    private static final String FLAG_TICKER = "k";
    private static final String FLAG_CONTENT = "c";
    private static final String FLAG_IMAGE_URL = "i";
    private static final String FLAG_CONTENT_URL = "u";

    private static final Options notifyCmdOption = new Options()
            .addOption(new Option(FLAG_TITLE, true, "Notification's title"))
            .addOption(new Option(FLAG_TICKER, true, "Notification's ticket text"))
            .addOption(new Option(FLAG_CONTENT, true, "Notification's content text"))
            .addOption(new Option(FLAG_IMAGE_URL, true, "Notification's Big image url"))
            .addOption(new Option(FLAG_CONTENT_URL, true, "URL to open on notification click"));

    private String title, ticker, content, imageUrl, contentUrl;

    public NotificationCommand(final String command) throws BaseCommand.CommandException, ParseException {

        super(command);

        final CommandLine cmd = getCmd();
        this.title = cmd.getOptionValue(FLAG_TITLE);
        this.ticker = cmd.getOptionValue(FLAG_TICKER);
        this.content = cmd.getOptionValue(FLAG_CONTENT);
        this.imageUrl = cmd.getOptionValue(FLAG_IMAGE_URL);
        this.contentUrl = cmd.getOptionValue(FLAG_CONTENT_URL);

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

    @Override
    public Options getOptions() {
        return notifyCmdOption;
    }

    public String getTicker() {
        return ticker;
    }
}
