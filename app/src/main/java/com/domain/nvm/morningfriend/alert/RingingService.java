package com.domain.nvm.morningfriend.alert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.R;

public class RingingService extends Service {

    private static final float VOLUME_STEP = 0.1f;

    private final IBinder mBinder = new RingingBinder();

    private boolean isPlaying;
    private float volume = 0.5f;
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
        RingingState.get().setRinging(true);
        AlarmWakeLock.acquireLock(this);
        setupPlayer();

    }

    private void setupPlayer() {
        String ringtoneSetting = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.prefs_ringtone_key), null);
        Uri ringtone;
        if (ringtoneSetting == null) {
            ringtone = RingtoneManager.getActualDefaultRingtoneUri(this.getApplicationContext(),
                            RingtoneManager.TYPE_RINGTONE);
        }
        else {
            ringtone = Uri.parse(ringtoneSetting);
        }
        mp = MediaPlayer.create(this, ringtone);

        mp.setLooping(true);
        mp.setVolume(volume, volume);
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2,
                0
        );
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

    public void muteSound() {
        if (isPlaying) {
            volume = 0f;
            mp.setVolume(0f, 0f);
        }
    }

    public void increaseVolume() {
        if (isPlaying && volume < 1.0f) {
            volume += VOLUME_STEP;
            mp.setVolume(volume, volume);
        }
    }

    public void decreaseVolume() {
        if (isPlaying && volume >= VOLUME_STEP) {
            volume -= VOLUME_STEP;
            mp.setVolume(volume, volume);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        RingingState.get().setRinging(false);
        AlarmWakeLock.releaseLock(this);
    }
}
