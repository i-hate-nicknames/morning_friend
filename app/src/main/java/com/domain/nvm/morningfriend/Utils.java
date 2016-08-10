package com.domain.nvm.morningfriend;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static String formatDate(Date date) {
        SimpleDateFormat format =
                new SimpleDateFormat("HH:mm:ss, dd.MM", java.util.Locale.getDefault());
        return format.format(date);
    }

    public static Date calculateAlertTime(Alarm alarm) {
        Calendar alertTime = Calendar.getInstance();
        alertTime.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        alertTime.set(Calendar.MINUTE, alarm.getMinute());
        alertTime.set(Calendar.SECOND, 0);
        alertTime.set(Calendar.MILLISECOND, 0);
        if (alertTime.getTime().getTime() < System.currentTimeMillis()) {
            alertTime.add(Calendar.DATE, 1);
        }
        return alertTime.getTime();
    }

    public static int getHour(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        return c.get(Calendar.MINUTE);
    }

    public static Date getDateWithHourMinute(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c.getTime();
    }
}
