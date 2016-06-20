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

    public static long getAlarmTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(ALARM_TIME, 0);
    }

    public static void setAlarmTime(Context context, long alarmTime) {
        if (alarmTime < System.currentTimeMillis()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(alarmTime));
            calendar.add(Calendar.DATE, 1);
            alarmTime = calendar.getTime().getTime();
        }
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(ALARM_TIME, alarmTime)
                .apply();
    }

}
