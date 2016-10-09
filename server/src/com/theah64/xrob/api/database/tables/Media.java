package com.theah64.xrob.api.database.tables;

import com.sun.istack.internal.Nullable;
import com.theah64.xrob.api.models.MediaNode;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by theapache64 on 9/10/16.
 */
public class Media extends BaseTable<MediaNode> {

    private static final Media instance = new Media();

    private Media() {
        super("media");
    }

    public static Media getInstance() {
        return instance;
    }

    @Override
    public void addv2(@Nullable String victimId, JSONArray jsonArray) throws RuntimeException, JSONException {

        //TODO : Loop through the JSON array and add media nodes to the db.

    }
}
