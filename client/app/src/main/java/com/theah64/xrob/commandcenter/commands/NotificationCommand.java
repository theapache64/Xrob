package com.theah64.xrob.commandcenter.commands;

import org.apache.commons.cli.*;

/**
 * Created by theapache64 on 14/9/16,7:52 PM.
 */
public class NotificationCommand extends BaseCommand {

    private static final String OPTION_TITLE = "t";
    private static final String OPTION_CONTENT = "c";
    private static final String OPTION_IMAGE_URL = "i";
    private static final String OPTION_CONTENT_URL = "u";

    private static final Options notifyCmdOption = new Options()
            .addOption(new Option(OPTION_TITLE, true, "Title"))
            .addOption(new Option(OPTION_CONTENT, true, "Content"))
            .addOption(new Option(OPTION_IMAGE_URL, true, "Image URL"))
            .addOption(new Option(OPTION_CONTENT_URL, true, "URL to open on click"));

    private String title, content, imageUrl, contentUrl;

    public NotificationCommand(final String command) throws BaseCommand.CommandException {

        super(command);
        final CommandLineParser parser = new DefaultParser();
        try {
            final CommandLine cmd = parser.parse(notifyCmdOption, getArgs());
            this.title = cmd.getOptionValue(OPTION_TITLE);
            this.content = cmd.getOptionValue(OPTION_CONTENT);
            this.imageUrl = cmd.getOptionValue(OPTION_IMAGE_URL);
            this.contentUrl = cmd.getOptionValue(OPTION_CONTENT_URL);

        } catch (ParseException e) {
            e.printStackTrace();
            throw new BaseCommand.CommandException("Invalid command: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "NotificationCommand{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
