package com.domain.nvm.morningfriend.alert;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.R;

public class RingingService extends Service {

    private final IBinder mBinder = new RingingBinder();

    private boolean isPlaying;
    private MediaPlayer mp;

    public class RingingBinder extends Binder {
        RingingService getService() {
            return RingingService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.write(this, "RingingService::onStartCommand");
        startPlaying();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.write(this, "RingingService::onCreate");
        AlarmWakeLock.acquireLock(this);
        mp = MediaPlayer.create(this, R.raw.eh);
        mp.setLooping(true);
    }

    public void startPlaying() {
        if (!isPlaying) {
            isPlaying = true;
            mp.start();
        }
    }

    public void stopPlaying() {
        isPlaying = false;
        mp.stop();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        AlarmWakeLock.releaseLock(this);
    }
}
