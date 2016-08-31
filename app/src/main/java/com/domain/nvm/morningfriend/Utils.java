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

    public static String formatTime(int hour, int minute) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, minute);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        return format.format(time.getTime());
    }

    public static String formatRemainingTime(long time) {
        long timeMins = time / (1000 * 60);
        String mins = Long.toString(timeMins % 60);
        String hours = Long.toString(timeMins / 60);
        return String.format(java.util.Locale.getDefault(), "%s hours, %s minutes", hours, mins);

    }

    public static Date calculateAlertTime(Alarm alarm) {
        Calendar alertTime = Calendar.getInstance();
        int daysOffset = getDaysOffset(alertTime.get(Calendar.DAY_OF_WEEK), alarm.getRepeatDays());
        if (daysOffset == -1) {
            return new Date(Long.MAX_VALUE);
        }
        alertTime.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        alertTime.set(Calendar.MINUTE, alarm.getMinute());
        alertTime.set(Calendar.SECOND, 0);
        alertTime.set(Calendar.MILLISECOND, 0);
        alertTime.add(Calendar.DATE, daysOffset);
        if (alertTime.getTime().getTime() < System.currentTimeMillis()) {
            alertTime.add(Calendar.DATE, 1);
        }
        return alertTime.getTime();
    }

    /**
     * Get number of days between given day (in Calendar format, starting with SUN=1) and the first
     * active day in repeatingDays
     * -1 if there are no active days
     * @param repeatingDays
     * @return
     */
    public static int getDaysOffset(int day, Alarm.Days repeatingDays) {
        int dayIdx = day-1;
        return repeatingDays.getClosestDayIndex(dayIdx);
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
