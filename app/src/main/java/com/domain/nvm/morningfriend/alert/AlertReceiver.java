package com.domain.nvm.morningfriend.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.Utils;
import com.domain.nvm.morningfriend.alert.puzzles.squares.SquaresActivity;
import com.domain.nvm.morningfriend.scheduler.AlarmScheduler;

import java.util.Date;

public class AlertReceiver extends BroadcastReceiver {

    private static final String EXTRA_ALARM = "alarm";

    public static Intent newIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, AlertReceiver.class);
        i.putExtra(EXTRA_ALARM, alarm);
        return i;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm alarm = (Alarm) intent.getSerializableExtra(EXTRA_ALARM);
        AlarmWakeLock.acquireLock(context);
        Intent i = SquaresActivity.newIntent(context, alarm);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
