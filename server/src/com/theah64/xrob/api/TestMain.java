package com.theah64.xrob.api;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {


    public static void main(String[] args) {
        try {
            final JSONObject jsonObject = new JSONObject("{\"multicast_id\":9101085366560335979,\"success\":1,\"failure\":0,\"canonical_ids\":0,\"results\":[{\"message_id\":\"0:1473859731326881%785c5b72f9fd7ecd\"}]}");
            System.out.println(jsonObject.getInt("success"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

