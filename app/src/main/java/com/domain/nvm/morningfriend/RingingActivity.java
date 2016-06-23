package com.domain.nvm.morningfriend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import java.util.Date;


public class RingingActivity extends SingleFragmentActivity implements RingingControls {

    private RingingService mService;
    private boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RingingService.RingingBinder binder = (RingingService.RingingBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    public Fragment getFragment() {
        return new StopSnoozeFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RingingService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        // this won't create another instance of service, but will call onStartCommand
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public static void setRingingAlarm(Context context, Date time, boolean isOn) {
        Intent i = new Intent(context, RingingActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (isOn) {
            am.set(AlarmManager.RTC_WAKEUP, time.getTime(), pi);
        }
        else {
            am.cancel(pi);
            pi.cancel();
        }
    }

    @Override
    public void stopRinging() {
        mService.stopPlaying();
        // todo: add setting "recurrent alarm" and check if it's true here
        AlarmPreferences.revalidateAlarmTime(this);
        Date nextAlarm = AlarmPreferences.getAlarmTime(this);
        setRingingAlarm(this, nextAlarm, true);
    }

    @Override
    public void stopAndRestartRinging(Date restartTime) {
        mService.stopPlaying();
        setRingingAlarm(this, restartTime, true);
    }
}
