package com.domain.nvm.morningfriend.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.Utils;
import com.domain.nvm.morningfriend.alert.puzzles.squares.SquaresActivity;
import com.domain.nvm.morningfriend.scheduler.AlarmSettings;

import java.util.Date;

public class AlertReceiver extends BroadcastReceiver {

    private static final String EXTRA_ALARM_TIME = "alarmTime";

    public static Intent newIntent(Context context, Date alarmTime) {
        Intent i = new Intent(context, AlertReceiver.class);
        i.putExtra(EXTRA_ALARM_TIME, alarmTime);
        return i;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.write(context, "AlertReceiver::onReceive");
        Logger.write(context, "Alarm time stored in settings: "
                + Utils.formatDate(AlarmSettings.getAlarmTime(context)));
        Date time = (Date) intent.getSerializableExtra(EXTRA_ALARM_TIME);
        if (time != null) {
            Logger.write(context, "Alarm time stored in Intent: " + Utils.formatDate(time));
        }
        else {
            Logger.write(context, "No time value was stored in Intent");
        }
        AlarmWakeLock.acquireLock(context);
        Intent i = new Intent(context, SquaresActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
