package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 10/10/16.
 */
public class MenuItem {

    private final String title;
    private final int count;
    private final String link;

    public MenuItem(String title, int count, String link) {
        this.title = title;
        this.count = count;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return count;
    }

    public String getLink() {
        return link;
    }
}
