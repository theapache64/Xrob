package com.theah64.xrob.api.models;

/**
 * Created by theapache64 on 10/10/16.
 */
public class MenuItem {

    private final String title;
    private final int menuId,count;
    private final String link;

    public MenuItem(int menuId, String title, int count, String link) {
        this.menuId = menuId;
        this.title = title;
        this.count = count;
        this.link = link;
    }

    public int getMenuId() {
        return menuId;
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
