package com.domain.nvm.morningfriend;

import android.content.Context;
import android.preference.PreferenceManager;

public class AlarmPreferences {

    private static final String IS_ENABLED = "isAlarmEnabled";

    public static boolean isEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_ENABLED, false);
    }

    public static void setEnabled(Context context, boolean isEnabled) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_ENABLED, isEnabled)
                .apply();
    }

}
