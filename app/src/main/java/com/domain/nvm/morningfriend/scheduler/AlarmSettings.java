package com.domain.nvm.morningfriend.scheduler;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.domain.nvm.morningfriend.Alarm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmSettings {

    private static final String TAG = "AlarmSettings";

    private static final String IS_ENABLED = "isAlarmEnabled";
    private static final String ALARM_TIME = "alarmTime";
    private static final String DIFFICULTY = "difficulty";

    private static Alarm alarm;

    public static Alarm getAlarm(Context context) {
        if (alarm == null) {
            // generate alarm
            boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(context)
                                    .getBoolean(IS_ENABLED, false);
            int diffPos = PreferenceManager.getDefaultSharedPreferences(context)
                            .getInt(DIFFICULTY, 0);
            long timeMillis = PreferenceManager.getDefaultSharedPreferences(context)
                    .getLong(ALARM_TIME, 0);
            alarm = new Alarm(0, new Date(timeMillis));
            alarm.setDifficulty(diffPos);
            alarm.setEnabled(isEnabled);
        }
        return alarm;
    }


    public static boolean isEnabled(Context context) {
        return getAlarm(context).isEnabled();
    }

    public static void setEnabled(Context context, boolean isEnabled) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_ENABLED, isEnabled)
                .apply();
        getAlarm(context).setEnabled(isEnabled);
    }

    public static int getDifficultyIndex(Context context) {
        return getAlarm(context).getDifficulty().ordinal();
    }

    public static Alarm.Difficulty getDifficulty(Context context) {
        return getAlarm(context).getDifficulty();
    }

    public static void setDifficulty(Context context, int difficulty) {
        try {
            getAlarm(context).setDifficulty(difficulty);
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putInt(DIFFICULTY, difficulty)
                    .apply();
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            Log.e(TAG, "Invalid difficulty code" + difficulty);
        }
    }

    /**
     * Get time at which next alarm should occur
     * if there is no setting saved, return next day 00:00:00
     * @param context
     * @return time for the next alarm
     */
    public static Date getAlarmTime(Context context) {
        return getAlarm(context).getTime();
    }

    public static void setAlarmTime(Context context, Date alarmTime) {
        getAlarm(context).setTime(alarmTime);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(ALARM_TIME, getAlarm(context).getTime().getTime())
                .apply();
    }

    public static void setRingingAlarm(Context context, Date time, boolean isOn) {
        getAlarm(context).schedule(context, isOn);
    }

    public static void setNextAlarm(Context context) {
        getAlarm(context).setNextAlarm(context);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format =
                new SimpleDateFormat("HH:mm:ss, dd.MM", java.util.Locale.getDefault());
        return format.format(date);
    }

}
