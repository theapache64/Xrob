package com.theah64.xrob.api;


import com.theah64.xrob.api.utils.DarKnight;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {
    public static void main(String[] args) {
        final String string = "Shifar";
        final String enc = DarKnight.getEncrypted(string);
        final String dec = DarKnight.getDecrypted(enc);

        System.out.println("String is " + string);
        System.out.println("Enc : " + enc);
        System.out.println("Dec : " + dec);
    }
}
