package com.domain.nvm.morningfriend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class AlarmScheduler {

    private static final String IS_ENABLED = "isAlarmEnabled";
    private static final String ALARM_TIME = "alarmTime";

    public static boolean isEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_ENABLED, false);
    }

    public static void setEnabled(Context context, boolean isEnabled) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_ENABLED, isEnabled)
                .apply();
    }

    /**
     * Get time at which next alarm should occur
     * if there is no setting saved, return next day 00:00:00
     * @param context
     * @return time for the next alarm
     */
    public static Date getAlarmTime(Context context) {
        long timeMillis = PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(ALARM_TIME, 0);
        if (timeMillis == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            timeMillis = calendar.getTimeInMillis();
        }
        return new Date(timeMillis);
    }

    public static void setAlarmTime(Context context, Date alarmTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(alarmTime);
        calendar.set(Calendar.SECOND, 0);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(ALARM_TIME, calendar.getTime().getTime())
                .apply();
        revalidateAlarmTime(context);
    }

    /**
     * revalidate currently stored setting, updating it to be the same time next day in case time
     * has already passed today
     * @param context
     */
    public static void revalidateAlarmTime(Context context) {
        Date storedTime = getAlarmTime(context);
        if (storedTime.getTime() < System.currentTimeMillis()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(storedTime);
            calendar.add(Calendar.DATE, 1);
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putLong(ALARM_TIME, calendar.getTime().getTime())
                    .apply();
        }
    }

    public static void setRingingAlarm(Context context, Date time, boolean isOn) {
        Intent i = new Intent(context, RingingActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            am.set(AlarmManager.RTC_WAKEUP, time.getTime(), pi);
        }
        else {
            am.cancel(pi);
            pi.cancel();
        }
    }

}
