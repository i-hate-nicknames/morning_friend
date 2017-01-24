package com.domain.nvm.morningfriend.alert;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.domain.nvm.morningfriend.R;


public class RingtonePlayer {

    private static final float PLAYER_VOLUME = 0.35f;

    private Context mContext;
    private boolean isPlaying;
    private int initialSystemVolume;
    private MediaPlayer mp;
    private AudioManager mAudioManager;

    public RingtonePlayer(Context context) {
        mContext = context;

    }

    private void setupPlayer() {
        String ringtoneSetting = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(mContext.getString(R.string.prefs_ringtone_key), null);
        Uri ringtone;
        if (ringtoneSetting == null) {
            ringtone = RingtoneManager.getActualDefaultRingtoneUri(mContext.getApplicationContext(),
                    RingtoneManager.TYPE_RINGTONE);
        }
        else {
            ringtone = Uri.parse(ringtoneSetting);
        }
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        initialSystemVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        mp = MediaPlayer.create(mContext, ringtone);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);
        mp.setVolume(PLAYER_VOLUME, PLAYER_VOLUME);
    }

    public void play() {
        if (!isPlaying) {
            isPlaying = true;
            setupPlayer();
            mp.start();
        }
    }

    public void stop() {
        if (isPlaying) {
            isPlaying = false;
            mp.stop();
            mp.release();
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initialSystemVolume, 0);
        }
    }
}
