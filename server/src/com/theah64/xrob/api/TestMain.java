package com.theah64.xrob.api;


import com.theah64.xrob.api.utils.clientpanel.commandcenter.commands.NotificationCommand;
import org.apache.commons.cli.*;


/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {


    public static void main(String[] args) {

        System.out.println(new NotificationCommand("-t TheTitle"));
    }


}

