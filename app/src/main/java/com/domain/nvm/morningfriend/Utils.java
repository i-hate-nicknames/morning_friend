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

    /**
     * Extract hour and minute from given timestamp
     * and find closest point in future for an alarm to fire at
     * @param time
     * @return
     */
    public static Date createAlarmTime(Date time) {
        Calendar givenTime = Calendar.getInstance();
        givenTime.setTime(time);
        Calendar nextAlarm = Calendar.getInstance();
        nextAlarm.set(Calendar.HOUR_OF_DAY, givenTime.get(Calendar.HOUR_OF_DAY));
        nextAlarm.set(Calendar.MINUTE, givenTime.get(Calendar.MINUTE));
        nextAlarm.set(Calendar.SECOND, 0);
        if (nextAlarm.getTime().getTime() <= System.currentTimeMillis()) {
            nextAlarm.add(Calendar.DATE, 1);
        }
        return nextAlarm.getTime();
    }


}
