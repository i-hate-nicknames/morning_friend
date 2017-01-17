package com.domain.nvm.morningfriend.alert;

import android.content.Context;
import android.os.PowerManager;

import com.domain.nvm.morningfriend.ui.logs.Logger;

public class AlarmWakeLock {

    private static final String TAG = "MorningFriend";

    private static PowerManager.WakeLock sWakeLock;

    public static void acquireLock(Context context) {
        Logger.write(context, "AlarmWakeLock::acquireLock");
        if (sWakeLock != null) {
            return;
        }

        Logger.write(context, "Acquiring wake lock");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sWakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, TAG);
        sWakeLock.acquire();
    }

    public static void releaseLock(Context context) {
        if (sWakeLock != null) {
            Logger.write(context, "Releasing wake lock");
            sWakeLock.release();
            sWakeLock = null;
        }
    }
}
