package com.theah64.xrob.api;

import com.theah64.xrob.api.database.utils.InsertQuery;

/**
 * Created by Shifar Shifz on 10/13/2015.
 */
public class TestMain {
    public static void main(String[] args) {
        final InsertQuery iQuery = new InsertQuery();
        final String insertQuery = iQuery
                .insertInto("deliveries")
                .set("name", "?")
                .set("age", "18")
                .set("something", "somethingelse")
                .build();

        System.out.println(insertQuery);
    }
}
