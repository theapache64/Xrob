package com.theah64.xrob.api.utils;

import com.theah64.xrob.api.database.tables.Users;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shifar on 31/12/15.
 */
public final class HeaderSecurity {

    public static final String KEY_AUTHORIZATION = "Authorization";
    private static final String REASON_API_KEY_MISSING = "API key is missing";
    private static final String REASON_INVALID_API_KEY = "Invalid API key";
    private final String authorization;
    private String userId;

    public HeaderSecurity(final String authorization) {
        //Collecting header from passed request
        this.authorization = authorization;
    }

    /**
     * Used to identify if passed API-KEY has a valid user.
     */
    public boolean isAuthorized() {

        if (this.authorization == null) {
            //No api key passed along with request
            return false;
        }

        final Users users = Users.getInstance();
        this.userId = users.get(Users.COLUMN_API_KEY, this.authorization, Users.COLUMN_ID);
        return this.userId != null;
    }

    public String getUserId() {
        if (this.userId == null) {
            throw new IllegalArgumentException("You must call isAuthorized() and return true to call getUserId()");
        }
        return this.userId;
    }

    public String getFailureReason() {
        return this.authorization == null ? REASON_API_KEY_MISSING : REASON_INVALID_API_KEY;
    }
}
