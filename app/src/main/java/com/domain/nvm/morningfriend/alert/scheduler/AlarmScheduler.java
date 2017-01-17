package com.domain.nvm.morningfriend.alert.scheduler;

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
    private static final String PREF_INTERRUPTED_SNOOZE = "pref_snooze";
    private static final long LATE_OFFSET = 3 * 1000;
    private static final long INTERRUPTED_ALARM_DELAY = 7 * 1000;

    /**
     * Find closest possible (in time) alarm to be fired. Alarm is to be chosen among current active
     * alarms and current snooze state.
     * @param context
     * @return
     */
    public static Alarm getClosestAlarm(Context context) {
        Alarm found = getSnoozeAlarm(context, PREF_INTERRUPTED_SNOOZE);
        if (found != null) {
            return found;
        }
        found = getSnoozeAlarm(context, PREF_SNOOZE);
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
    private static Alarm getSnoozeAlarm(Context context, String snoozePrefKey) {
        String pref = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(snoozePrefKey, null);
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
        alarm.setSnooze(true);
        return alarm;
    }

    /**
     * Choose closest possible alarm from active alarms and current snooze state,
     * then register it in the system to be fired at the time specified in alarm
     * Cancel active alarm if no alarm is found
     * At most one alarm can be active at the same time
     * @param context
     */
    public static void setNextAlarm(Context context) {
        Alarm next = getClosestAlarm(context);
        Intent i = AlertReceiver.makeIntent(context, next);
        PendingIntent pi =
                PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (next == null) {
            // intent inside pi has null as extra, but that's okay with cancelling
            // because when pi is cancelled their intents' extas are not compared
            am.cancel(pi);
            return;
        }
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


    public static void puzzleInterruptedSnooze(Context context, Alarm alarm) {
        long snoozeTime = System.currentTimeMillis() + INTERRUPTED_ALARM_DELAY;
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_INTERRUPTED_SNOOZE, alarm.getId() + "_" + snoozeTime)
                .apply();
        setNextAlarm(context);
    }

}
