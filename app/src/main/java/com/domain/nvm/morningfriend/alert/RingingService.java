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

import com.domain.nvm.morningfriend.ui.logs.Logger;
import com.domain.nvm.morningfriend.R;

public class RingingService extends Service {

    private static final float VOLUME_STEP = 0.01f;
    private static final float PLAYER_MAX_VOLUME = 0.35f;

    private final IBinder mBinder = new RingingBinder();

    private boolean isPlaying;
    private float playerVolume = 0.05f;
    private int initialSystemVolume;
    private MediaPlayer mp;
    private AudioManager mAudioManager;

    public class RingingBinder extends Binder {
        public RingingService getService() {
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
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initialSystemVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maximizeSystemVolume();
        mp = MediaPlayer.create(this, ringtone);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);
        mp.setVolume(playerVolume, playerVolume);
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
            playerVolume = 0f;
            mp.setVolume(0f, 0f);
        }
    }

    public void increaseVolume() {
        if (isPlaying && playerVolume < PLAYER_MAX_VOLUME) {
            playerVolume += VOLUME_STEP;
            mp.setVolume(playerVolume, playerVolume);
        }
    }

    public void decreaseVolume() {
        if (isPlaying && playerVolume >= VOLUME_STEP) {
            playerVolume -= VOLUME_STEP;
            mp.setVolume(playerVolume, playerVolume);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initialSystemVolume, 0);
        AlarmWakeLock.releaseLock(this);
    }

    public void maximizeSystemVolume() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
            mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0);
    }
}
