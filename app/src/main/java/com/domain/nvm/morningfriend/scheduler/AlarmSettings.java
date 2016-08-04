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

    public static boolean isEnabled(Context context) {
        // stub method for old functionality for single alarm
        return false;
    }


    public static Alarm.Difficulty getDifficulty(Context context) {
        return Alarm.Difficulty.EASY;
    }

    /**
     * Get time at which next alarm should occur
     * if there is no setting saved, return next day 00:00:00
     * @param context
     * @return time for the next alarm
     */
    public static Date getAlarmTime(Context context) {
        return null;
    }


    public static void setRingingAlarm(Context context, Date time, boolean isOn) {

    }

    public static void setNextAlarm(Context context) {

    }

}
