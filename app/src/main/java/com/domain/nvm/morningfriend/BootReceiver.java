package com.domain.nvm.morningfriend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isOn = AlarmScheduler.isEnabled(context);
        Date time = AlarmScheduler.getAlarmTime(context);
        AlarmScheduler.setRingingAlarm(context, time, isOn);
    }
}
