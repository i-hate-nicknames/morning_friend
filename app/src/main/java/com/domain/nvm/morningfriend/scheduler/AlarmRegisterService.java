package com.domain.nvm.morningfriend.scheduler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.domain.nvm.morningfriend.scheduler.AlarmScheduler;

import java.util.Date;

public class AlarmRegisterService extends Service {
    public AlarmRegisterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: find the soonest alarm and schedule it
        boolean isOn = AlarmScheduler.isEnabled(getApplicationContext());
        Date time = AlarmScheduler.getAlarmTime(getApplicationContext());
        AlarmScheduler.setRingingAlarm(getApplicationContext(), time, isOn);
        return START_NOT_STICKY;
    }
}
