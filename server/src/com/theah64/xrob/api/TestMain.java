package com.theah64.xrob.api;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {


    public static void main(String[] args) throws JSONException {

        System.out.println(new FileWalker("lab/testdir").walk());


    }


}

