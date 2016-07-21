package com.domain.nvm.morningfriend.alert;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class AlarmWakeLock {

    private static final String TAG = "MorningFriend";

    private static PowerManager.WakeLock sWakeLock;

    public static void acquireLock(Context context) {
        if (sWakeLock != null) {
            return;
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sWakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, TAG);
        sWakeLock.acquire();
    }

    public static void releaseLock() {
        if (sWakeLock != null) {
            sWakeLock.release();
            sWakeLock = null;
        }
    }
}
