package com.think42lab.arangam.util;

import android.provider.SyncStateContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by think42lab on 18/12/16.
 */

public class DateUtil {

    public static final String DEFAULT_TIME_ZONE = "Asia/Kolkata";

    public static Calendar convertStringToCalendar(String dateString, String timeFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        format.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        try {
            cal.setTime(format.parse(dateString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String convertDateObjectToString(Date date, String timeFormat) {
        String formattedDate = null;
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        format.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        try {
            formattedDate = format.format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String getDayOfMonthSuffix(final int n) {

        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static Calendar getCalendarWithoutTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
