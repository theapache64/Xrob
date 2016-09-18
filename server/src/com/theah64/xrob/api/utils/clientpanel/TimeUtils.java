package com.theah64.xrob.api.utils.clientpanel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by theapache64 on 13/9/16,10:41 PM.
 */
public class TimeUtils {
    private static List<Long> milliList = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1)
    );

    private static List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

    public static String getRelativeTime(final boolean isInMillis, long timestamp) {

        final long diff = System.currentTimeMillis() - (isInMillis ? timestamp : (timestamp * 1000));

        final StringBuilder timeBuilder = new StringBuilder();

        for (int i = 0; i < milliList.size(); i++) {
            final Long current = milliList.get(i);
            final long temp = diff / current;
            if (temp > 0) {
                timeBuilder.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                break;
            }
        }

        if (timeBuilder.toString().isEmpty()) {
            return "0 second ago";
        }

        return timeBuilder.toString();
    }
}
