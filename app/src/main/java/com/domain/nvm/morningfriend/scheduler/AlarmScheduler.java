package com.domain.nvm.morningfriend.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.Utils;
import com.domain.nvm.morningfriend.alert.AlertReceiver;
import com.domain.nvm.morningfriend.database.AlarmRepository;

public class AlarmScheduler {

    private static final String PREF_SNOOZE = "pref_snooze";
    private static final long LATE_OFFSET = 0;

    private static Alarm getClosestAlarm(Context context) {
        Alarm found = getSnoozeAlarm(context);
        if (found != null) {
            return found;
        }
        long minTime = Long.MAX_VALUE;
        for (Alarm a: AlarmRepository.get(context).getAlarms()) {
            if (!a.isEnabled()) {
                continue;
            }
            long time = Utils.calculateAlertTime(a).getTime();
            if (time < minTime) {
                minTime = time;
                found = a;
                a.setTime(time);
            }
        }
        return found;
    }

    /**
     * Check if snooze shared pref is set and the time to fire is valid and return Alarm
     * corresponding to this snooze setting
     * @param context
     * @return alarm
     */
    private static Alarm getSnoozeAlarm(Context context) {
        String pref = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SNOOZE, null);
        if (pref == null) {
            return null;
        }
        String[] snoozeVals = pref.split("_");
        int id = Integer.parseInt(snoozeVals[0]);
        Alarm alarm = AlarmRepository.get(context).getAlarm(id);
        long time = Long.parseLong(snoozeVals[1]);
        if (time - LATE_OFFSET < System.currentTimeMillis()) {
            return null;
        }
        alarm.setTime(time);
        return alarm;
    }

    public static void setNextAlarm(Context context) {
        Alarm next = getClosestAlarm(context);
        if (next == null) {
            return;
        }
        Intent i = AlertReceiver.newIntent(context, next);
        PendingIntent pi =
                PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 19) {
            am.setExact(AlarmManager.RTC_WAKEUP, next.getTime(), pi);
        }
        else {
            am.set(AlarmManager.RTC_WAKEUP, next.getTime(), pi);
        }
    }

    /**
     * Register snooze for given alarm. Snooze time is calculated using current time and alarm
     * settings. Results in setting shared preference corresponding to snooze state. Only one
     * snooze can be registered, consequent registering will overwrite older ones.
     * @param context
     * @param alarm
     */
    public static void snooze(Context context, Alarm alarm) {
        long snoozeTime = System.currentTimeMillis() + alarm.getSnoozeDuration();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SNOOZE, alarm.getId() + "_" + snoozeTime)
                .apply();
        setNextAlarm(context);
    }

}
