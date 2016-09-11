package com.theah64.xrob.utils;

import android.util.Log;

import com.theah64.xrob.models.Victim;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by shifar on 29/7/16.
 * Utility class to create API request object.
 */
public class APIRequestBuilder {

    public static final String BASE_URL = "http:/google.com";

    private static final String X = APIRequestBuilder.class.getSimpleName();
    private static final String KEY_AUTHORIZATION = "Authorization";

    final Request.Builder requestBuilder = new Request.Builder();
    private final StringBuilder logBuilder = new StringBuilder();

    private final String url;
    private FormBody.Builder params = new FormBody.Builder();

    public APIRequestBuilder(String route, final boolean isAuthKeyNeeded) {

        this.url = BASE_URL + route;
        appendLog("URL", url);

        if (isAuthKeyNeeded) {
            requestBuilder.addHeader(KEY_AUTHORIZATION, Victim.getAPIKey());
            appendLog(KEY_AUTHORIZATION, Victim.getAPIKey());
        }
    }

    public APIRequestBuilder(String route) {
        this(route, false);
    }


    private void appendLog(String key, String value) {
        logBuilder.append(String.format("%s='%s'\n", key, value));
    }

    private APIRequestBuilder addParam(final boolean isAllowNull, final String key, final String value) {

        if (isAllowNull) {
            this.params.add(key, value);
            appendLog(key, value);
        } else {
            //value must be not null.
            if (value != null) {
                this.params.add(key, value);
                appendLog(key, value);
            }
        }

        return this;
    }

    public APIRequestBuilder addParam(final String key, final String value) {
        return addParam(true, key, value);
    }


    /**
     * Used to build the OkHttpRequest.
     */
    public Request build() {

        requestBuilder
                .post(params.build())
                .url(url);

        Log.d(X, "Request : " + logBuilder.toString());

        return requestBuilder.build();
    }

    public APIRequestBuilder addParamIfNotNull(String key, String value) {
        return addParam(false, key, value);
    }
}
