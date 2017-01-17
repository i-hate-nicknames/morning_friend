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

    private static final float PLAYER_VOLUME = 0.35f;

    private final IBinder mBinder = new RingingBinder();

    private boolean isPlaying;
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
        return START_NOT_STICKY;
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
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        mp = MediaPlayer.create(this, ringtone);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);
        mp.setVolume(PLAYER_VOLUME, PLAYER_VOLUME);
    }

    public void startPlaying() {
        if (!isPlaying) {
            isPlaying = true;
            mp.start();
        }
    }

    public void stopPlaying() {
        Logger.write(this, "RingingService::stopPlaying");
        isPlaying = false;
        mp.stop();
        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.write(this, "RingingService::onDestroy");
        mp.release();
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initialSystemVolume, 0);
        AlarmWakeLock.releaseLock(this);
    }

}
