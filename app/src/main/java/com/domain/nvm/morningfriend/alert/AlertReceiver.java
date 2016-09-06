package com.domain.nvm.morningfriend.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.ui.logs.Logger;
import com.domain.nvm.morningfriend.ui.puzzle.squares.SquaresActivity;
import com.domain.nvm.morningfriend.ui.puzzle.untangle.UntangleActivity;

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
        Logger.write(context, "Alarm triggered!");
        AlarmWakeLock.acquireLock(context);
        Intent puzzleIntent = null;
        switch (alarm.getPuzzle()) {
            case GRAPH:
                puzzleIntent = UntangleActivity.newIntent(context, alarm);
                break;
            case SQUARES:
                puzzleIntent = SquaresActivity.newIntent(context, alarm);
                break;
        }
        puzzleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(puzzleIntent);
    }
}
