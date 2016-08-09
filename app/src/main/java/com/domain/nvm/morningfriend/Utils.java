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
}
