package com.domain.nvm.morningfriend.alert;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.scheduler.AlarmScheduler;


public abstract class PuzzleActivity extends AppCompatActivity {

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmWakeLock.acquireLock(this);
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

    public void stopAndRestartRinging(Alarm alarm) {
        mService.stopPlaying();
        AlarmScheduler.snooze(this, alarm);
    }

    public void showMuteMessage(String message) {
        if (message == null) {
            message = getString(R.string.snackbar_mute_message);
        }
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.snackbar_mute_button_text),
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mService.muteSound();
                    }
                })
                .show();
    }
}
