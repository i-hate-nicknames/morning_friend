package com.domain.nvm.morningfriend.scheduler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
        AlarmScheduler.setNextAlarm(this);
        return START_NOT_STICKY;
    }
}
