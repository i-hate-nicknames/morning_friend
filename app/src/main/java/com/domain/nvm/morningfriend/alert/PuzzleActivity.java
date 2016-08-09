package com.domain.nvm.morningfriend.alert;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.scheduler.AlarmScheduler;

import java.util.Date;


public abstract class PuzzleActivity extends AppCompatActivity {

    private RingingService mService;
    private boolean mBound = false;
    protected Alarm mAlarm;

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

    private static final String EXTRA_ALARM = "alarm";

    public static Intent newIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, AlertReceiver.class);
        i.putExtra(EXTRA_ALARM, alarm);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmWakeLock.acquireLock(this);
        mAlarm = (Alarm) getIntent().getSerializableExtra(EXTRA_ALARM);
        final Window win = getWindow();
        win.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
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


    public void stopRinging() {
        mService.stopPlaying();
        AlarmScheduler.setNextAlarm(this);
    }

    public void stopAndRestartRinging() {
        mService.stopPlaying();
        AlarmScheduler.snooze(this);
    }
}
