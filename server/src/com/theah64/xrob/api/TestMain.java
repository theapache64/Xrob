package com.theah64.xrob.api;


import com.theah64.xrob.api.utils.DarKnight;
import org.json.JSONException;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {

    public static void main(String[] args) throws JSONException, IOException {
        System.out.println(DarKnight.getEncrypted("localhost"));
    }


}

