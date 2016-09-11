package com.theah64.xrob.api;


import com.theah64.xrob.api.utils.PreparedUpdateQueryBuilder;

import javax.activation.MimeType;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println(
                new PreparedUpdateQueryBuilder("table1")
                        .setIfNotNull("key1", "value1", true)
                        .setIfNotNull("key2", "value2", true)
                        .setIfNotNull("key3", null, true)
                        .setIfNotNull("key4", "value4", false)
                        .setWhereClause("column1 = ? AND column2 = ?")
                        .build()


        );
    }
}
