package com.domain.nvm.morningfriend.features.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.domain.nvm.morningfriend.features.alarm.Alarm;
import com.domain.nvm.morningfriend.features.log.Logger;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleActivity;

public class AlertReceiver extends BroadcastReceiver {

    private static final String EXTRA_ALARM = "alarm";

    public static Intent makeIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, AlertReceiver.class);
        i.putExtra(EXTRA_ALARM, alarm);
        return i;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmWakeLock.acquireLock(context);
        Logger.write(context, "Alarm triggered!");
        Alarm alarm = (Alarm) intent.getSerializableExtra(EXTRA_ALARM);
        Intent puzzleActivity = PuzzleActivity.makeIntent(context, alarm);
        puzzleActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
            Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(puzzleActivity);
    }
}
