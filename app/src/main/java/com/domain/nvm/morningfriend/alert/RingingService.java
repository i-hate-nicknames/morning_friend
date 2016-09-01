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

import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.R;

public class RingingService extends Service {

    private static final float VOLUME_STEP = 0.015f;

    private final IBinder mBinder = new RingingBinder();

    private float playerVolume = 0.5f;
    private int initialSystemVolume;
    private MediaPlayer mp;

    public class RingingBinder extends Binder {
        RingingService getService() {
            return RingingService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.write(this, "RingingService::onStartCommand");
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
        mp.setVolume(playerVolume, playerVolume);
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initialSystemVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 4,
                0);
    }

    public void startPlaying() {
        mp.start();
    }

    public void stopPlaying() {
        mp.stop();
    }

    public void muteSound() {
        playerVolume = 0f;
        mp.setVolume(0f, 0f);
    }

    public void increaseVolume() {
        if (playerVolume < 1.0f) {
            playerVolume += VOLUME_STEP;
            mp.setVolume(playerVolume, playerVolume);
        }
    }

    public void decreaseVolume() {
        if (playerVolume >= VOLUME_STEP) {
            playerVolume -= VOLUME_STEP;
            mp.setVolume(playerVolume, playerVolume);
        }
    }

    public void stop() {
        stopPlaying();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, initialSystemVolume, 0);

    }
}
