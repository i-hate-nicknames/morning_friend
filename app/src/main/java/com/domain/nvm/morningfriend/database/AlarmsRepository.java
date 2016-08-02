package com.domain.nvm.morningfriend.database;

import android.content.Context;

import com.domain.nvm.morningfriend.Alarm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmsRepository {

    private static AlarmsRepository sInstance;
    private static Context sContext;
    private List<Alarm> mAlarms;

    public static AlarmsRepository get(Context context) {
        if (sInstance == null) {
            sInstance = new AlarmsRepository(context);
        }
        return sInstance;
    }

    public AlarmsRepository(Context context) {
        sContext = context.getApplicationContext();
        mAlarms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Alarm a = new Alarm(i, new Date(System.currentTimeMillis()));
            a.setEnabled(i % 2 == 0);
            mAlarms.add(a);
        }
    }

    public List<Alarm> getAlarms() {
        return mAlarms;
    }

    public void updateAlarm(Alarm alarm) {
        mAlarms.set(alarm.getId(), alarm);
    }

    public Alarm getAlarm(int id) {
        return mAlarms.get(id);
    }
}
