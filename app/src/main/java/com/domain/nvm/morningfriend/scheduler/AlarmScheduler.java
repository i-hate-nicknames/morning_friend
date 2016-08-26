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
        return null;
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
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("pref_snooze", alarm.getId() + "_" + alarm.getTime())
                .apply();
    }

}
