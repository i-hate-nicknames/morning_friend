package com.domain.nvm.morningfriend.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.alert.puzzles.squares.SquaresActivity;
import com.domain.nvm.morningfriend.alert.puzzles.untangle.UntangleActivity;
import com.domain.nvm.morningfriend.scheduler.AlarmSettings;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.write(context, "AlertReceiver::onReceive");
        Logger.write(context, "Alarm time stored in settings: "
                + AlarmSettings.formatDate(AlarmSettings.getAlarmTime(context)));
        AlarmWakeLock.acquireLock(context);
        Intent i = new Intent(context, SquaresActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
