package com.domain.nvm.morningfriend;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class AlarmPreferences {

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

    public static Date getAlarmTime(Context context) {
        long timeMillis = PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(ALARM_TIME, 0);
        if (timeMillis == 0) {
            Calendar calendar = Calendar.getInstance();
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
        if (alarmTime.getTime() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(ALARM_TIME, calendar.getTime().getTime())
                .apply();
    }

}
