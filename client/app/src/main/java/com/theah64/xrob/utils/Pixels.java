package com.theah64.xrob.utils;

import com.theah64.xrob.commandcenter.commands.NotificationCommand;

/**
 * Created by theapache64 on 8/10/16.
 */
public class Pixels {

    private static final Pixels instance = new Pixels();

    public static Pixels getInstance() {
        return instance;
    }

    public String getImageUrl(String imageName) {
        //TODO: Find some free image api and collect an image url here.
        return null;
    }
}
