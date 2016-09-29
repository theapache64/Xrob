package com.theah64.xrob.api.models;

import com.theah64.xrob.api.utils.clientpanel.TimeUtils;

/**
 * Created by theapache64 on 9/4/16.
 */
public class Message {
    private final String fromName, fromPhone, content, relativeDeliveryTime;

    public Message(String fromName, String fromPhone, String content, long deliveredAt) {
        this.fromName = fromName;
        this.fromPhone = fromPhone;
        this.content = content;
        this.relativeDeliveryTime = TimeUtils.getRelativeTime(true, deliveredAt);
    }

    public String getFromName() {
        return fromName;
    }

    public String getFromPhone() {
        return fromPhone;
    }

    public String getContent() {
        return content;
    }

    public String getRelativeDeliveryTime() {
        return relativeDeliveryTime;
    }
}
