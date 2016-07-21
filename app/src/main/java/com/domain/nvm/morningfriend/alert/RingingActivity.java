package com.domain.nvm.morningfriend.alert;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import com.domain.nvm.morningfriend.SingleFragmentActivity;
import com.domain.nvm.morningfriend.scheduler.AlarmScheduler;
import com.domain.nvm.morningfriend.alert.puzzles.untangle.UntangleFragment;

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
        return new UntangleFragment();
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



    @Override
    public void stopRinging() {
        mService.stopPlaying();
        // todo: add setting "recurrent alarm" and check if it's true here
        AlarmScheduler.revalidateAlarmTime(this);
        Date nextAlarm = AlarmScheduler.getAlarmTime(this);
        AlarmScheduler.setRingingAlarm(this, nextAlarm, true);
    }

    @Override
    public void stopAndRestartRinging(Date restartTime) {
        mService.stopPlaying();
        AlarmScheduler.setRingingAlarm(this, restartTime, true);
    }
}
