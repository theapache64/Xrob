package com.theah64.xrob.utils.gpix;

import android.util.Log;

import com.theah64.xrob.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shifar on 14/10/16.
 */
public class GPix {

    private static final String API_URL_FORMAT = "http://35.161.57.139:8080/gpix/v1/gpix?keyword=%s&limit=%d";
    private static final String AUTHORIZATION = "WYAfuHwjCu";
    private static final String X = GPix.class.getSimpleName();

    private static GPix instance = new GPix();

    public static GPix getInstance() {
        return instance;
    }


    public void search(String keyword, int limit, final com.theah64.xrob.utils.gpix.Callback callback) throws GPixException, IOException, JSONException {

        final String url = String.format(Locale.getDefault(), API_URL_FORMAT, getEncoded(keyword), limit);

        Log.d(X, "Url : " + url);

        //Building ok http request
        final Request gpixRequest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", AUTHORIZATION)
                .build();

        OkHttpUtils.getInstance().getClient().newCall(gpixRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonData = OkHttpUtils.logAndGetStringBody(response);

                try {
                    final JSONObject joResp = new JSONObject(jsonData);

                    final boolean hasError = joResp.getBoolean("error");
                    final String message = joResp.getString("message");

                    if (!hasError) {
                        final JSONArray jaImages = joResp.getJSONObject("data").getJSONArray("images");
                        callback.onResult(parse(jaImages));
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                }
            }
        });

    }

    private static String getEncoded(String data) throws UnsupportedEncodingException {
        return URLEncoder.encode(data, "UTF-8");
    }

    private static List<Image> parse(JSONArray jaImages) throws JSONException {
        List<Image> imageList = new ArrayList<Image>(jaImages.length());

        for (int i = 0; i < jaImages.length(); i++) {
            final JSONObject joImage = jaImages.getJSONObject(i);

            final String imageUrl = joImage.getString("image_url");
            final String thumbUrl = joImage.getString("thumb_url");
            final int width = joImage.getInt("width");
            final int height = joImage.getInt("height");

            imageList.add(new Image(thumbUrl, imageUrl, height, width));

        }
        return imageList;
    }

    public static class GPixException extends Exception {
        public GPixException(String message) {
            super(message);
        }
    }


}
