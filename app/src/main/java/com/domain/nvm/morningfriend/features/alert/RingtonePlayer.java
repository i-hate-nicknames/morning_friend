package com.domain.nvm.morningfriend.features.alert;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.domain.nvm.morningfriend.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class RingtonePlayer {

    private static final float PLAYER_VOLUME = 0.35f;

    final static int FOR_MEDIA = 1;
    final static int FORCE_NONE = 0;
    final static int FORCE_SPEAKER = 1;

    private Context mContext;
    private boolean isPlaying;
    private int initialSystemVolume;
    private int initialAudioManagerMode;
    private boolean initialSpeakerStateOn;
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
        setForceAudioToSpeakers(true);
    }

    /**
     * proudly stolen from
     * http://stackoverflow.com/questions/12036221/how-to-turn-speaker-on-off-programatically-in-android-4-0
     * Totally not an ugly hack
     * @param force
     */
    private void setForceAudioToSpeakers(boolean force) {
        try {
            Class audioSystemClass = Class.forName("android.media.AudioSystem");
            Method setForceUse = audioSystemClass.getMethod("setForceUse", int.class, int.class);
            if (force) {
                setForceUse.invoke(null, FOR_MEDIA, FORCE_SPEAKER);
            }
            else {
                setForceUse.invoke(null, FOR_MEDIA, FORCE_NONE);
            }
        }
        // if we can't do it for some reason then well this is life what can you do about it
        catch (NoSuchMethodException ex) {

        }
        catch (ClassNotFoundException ex) {

        }
        catch (InvocationTargetException ex) {

        }
        catch (IllegalAccessException ex) {

        }
    }

    private void cleanup() {
        mp.release();
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initialSystemVolume, 0);
        setForceAudioToSpeakers(false);
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
            cleanup();
        }
    }
}
