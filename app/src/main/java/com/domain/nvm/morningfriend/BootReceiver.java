package com.domain.nvm.morningfriend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isOn = AlarmPreferences.isEnabled(context);
        Date time = AlarmPreferences.getAlarmTime(context);
        RingingActivity.setRingingAlarm(context, time, isOn);
    }
}
