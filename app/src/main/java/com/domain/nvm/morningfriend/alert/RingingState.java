package com.domain.nvm.morningfriend.alert;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.database.AlarmRepository;

public class RingingState {

    private static final String STATE_ALARM_ID = "pref_state_alarm_id";

    public static Alarm getAlarm(Context context) {
        int alarmId = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(STATE_ALARM_ID, -1);
        if (alarmId != -1) {
            Log.i("sraka", "loading alarm " + alarmId);
            return AlarmRepository.get(context).getAlarm(alarmId);
        }
        Log.i("sraka", "Alarm not found sori");
        return null;
    }

    public static void saveAlarm(Context context, Alarm alarm) {
        Log.i("sraka", "saving alarm " + alarm.getId());
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(STATE_ALARM_ID, alarm.getId())
                .apply();
    }

    public static void removeAlarm(Context context) {
        Log.i("sraka", "removing alarm");
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(STATE_ALARM_ID, -1)
                .apply();
    }
}


