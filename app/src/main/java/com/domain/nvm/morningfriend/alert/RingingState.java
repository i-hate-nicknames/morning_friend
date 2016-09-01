package com.domain.nvm.morningfriend.alert;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.domain.nvm.morningfriend.Alarm;

public class RingingState {

    private static RingingState sState;

    private boolean isRinging;
    private Alarm mAlarm;
    private RingingService mService;
    private Context mContext;
    private boolean mBound = false;

    private static final String TAG = "RingingState";

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

    public static RingingState get(Context context) {
        if (sState == null) {
            sState = new RingingState(context);
        }
        return sState;
    }

    public RingingState(Context context) {
        Log.d(TAG, "creating ringing state");
        mContext = context.getApplicationContext();
        startService();
    }

    public Alarm getAlarm() {
        return mAlarm;
    }

    public void setAlarm(Alarm alarm) {
        mAlarm = alarm;
    }

    public boolean isRinging() {
        return isRinging;
    }

    public void start(Alarm alarm) {
        if (!isRinging) {
            Log.d(TAG, "RingingState::start");
            AlarmWakeLock.acquireLock(mContext);
            mAlarm = alarm;
            mService.startPlaying();
            isRinging = true;
        }
    }

    public void stop() {
        if (isRinging) {
            Log.d(TAG, "RingingState::stop");
            mService.stop();
            if (mBound) {
                mContext.unbindService(mConnection);
                mBound = false;
            }
            isRinging = false;
            AlarmWakeLock.releaseLock(mContext);
        }
    }

    public void volumeUp() {
        if (isRinging) {
            mService.increaseVolume();
        }
    }

    public void volumeDown() {
        if (isRinging) {
            mService.decreaseVolume();
        }
    }

    public void volumeOff() {
        if (isRinging) {
            mService.muteSound();
        }
    }

    private void startService() {
        Log.d(TAG, "RingingState::startService");
        Intent intent = new Intent(mContext, RingingService.class);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        // this won't create another instance of service, but will call onStartCommand
        mContext.startService(intent);
    }

}