package com.theah64.xrob.api.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by theapache64 on 14/9/16,6:07 PM.
 */
public class FCMUtils {

    private static final String FCM_SEND_URL = "https://fcm.googleapis.com/fcm/send";


    public static final String KEY_TYPE = "type";
    public static final String TYPE_COMMAND = "command";
    public static final String KEY_DATA = "data";
    public static final String KEY_TYPE_DATA = "type_data";
    public static final String KEY_TO = "to";
    private static final String FCM_NOTIFICATION_KEY = "AIzaSyAlI64y49SzETsHp_8-zasi4QZdAIZt7XM";

    public static boolean sendPayload(String payload) {

        System.out.println(payload);

        try {
            final URL url = new URL(FCM_SEND_URL);
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty("Authorization", "key=" + FCM_NOTIFICATION_KEY);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            OutputStream os = urlConnection.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            os.close();

            final BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line).append("\n");
            }
            br.close();
            final JSONObject joResp = new JSONObject(response.toString());
            return joResp.getInt("failure") == 0;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }


}
