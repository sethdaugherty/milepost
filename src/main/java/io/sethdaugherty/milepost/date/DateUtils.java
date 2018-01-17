package io.sethdaugherty.milepost.date;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateUtils {
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static long computeStartTimestamp(String date) {
        return computeStartTimestamp(date, UTC);
    }

    public static long computeEndTimestamp(String date) {
        return computeEndTimestamp(date, UTC);
    }

    public static long computeStartTimestamp(String date, TimeZone timezone) {
        return computeStartDateTime(date, timezone).getMillis();
    }

    public static long computeEndTimestamp(String date, TimeZone timezone) {
        return computeEndDateTime(date, timezone).getMillis();
    }

    public static DateTime computeStartDateTime(String date, TimeZone timezone) {
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(4, 6));
        int day = Integer.valueOf(date.substring(6, 8));

        DateTime dateTime = new DateTime(year, month, day, 0, 0, DateTimeZone.forTimeZone(timezone));

        return dateTime;
    }

    public static DateTime computeEndDateTime(String date, TimeZone timezone) {
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(4, 6));
        int day = Integer.valueOf(date.substring(6, 8));

        DateTime dateTime = new DateTime(year, month, day, 23, 59, 59, 999, DateTimeZone.forTimeZone(timezone));

        return dateTime;
    }
}
