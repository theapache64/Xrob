package com.theah64.xrob.api;


import com.theah64.xrob.api.utils.DarKnight;
import org.json.JSONException;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {


    public static void main(String[] args) throws JSONException {

        System.out.println(DarKnight.getEncrypted("xrob_server1.netne.net"));
        System.out.println(DarKnight.getEncrypted("a5054518"));
        System.out.println(DarKnight.getEncrypted("PwD5689847469"));

    }


}

