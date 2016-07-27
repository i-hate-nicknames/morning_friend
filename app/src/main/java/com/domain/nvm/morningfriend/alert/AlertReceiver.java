package com.domain.nvm.morningfriend.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.alert.puzzles.squares.SquaresActivity;
import com.domain.nvm.morningfriend.alert.puzzles.untangle.UntangleActivity;
import com.domain.nvm.morningfriend.scheduler.AlarmSettings;

import java.text.SimpleDateFormat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmWakeLock.acquireLock(context);
        Logger.write(context, "AlertReceiver::onReceive");
        SimpleDateFormat format =
                new SimpleDateFormat("HH:mm, dd.MM", java.util.Locale.getDefault());
        Logger.write(context, "Alarm time stored in settings: "
                + format.format(AlarmSettings.getAlarmTime(context)));
        Intent i = new Intent(context, SquaresActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
