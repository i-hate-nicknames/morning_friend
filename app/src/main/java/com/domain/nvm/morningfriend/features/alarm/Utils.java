package com.domain.nvm.morningfriend.features.alarm;

import android.content.Context;
import android.support.annotation.ArrayRes;

import com.domain.nvm.morningfriend.R;

public class Utils {

    private static String getArrayValueAt(Context context, @ArrayRes int arrayResId, int pos) {
        String[] array = context.getResources().getStringArray(arrayResId);
        return array[pos];
    }

    private static int getAlarmDiffPos(Alarm a) {
        return a.getDifficulty().ordinal();
    }

    private static int getAlarmPuzzlePos(Alarm a) {
        return a.getPuzzleType().ordinal();
    }

    public static String getAlarmDifficultyKey(Context context, Alarm alarm) {
        return getArrayValueAt(context, R.array.pref_difficulty_values, getAlarmDiffPos(alarm));
    }

    public static String getAlarmDifficultyTitle(Context context, Alarm alarm) {
        return getArrayValueAt(context, R.array.pref_difficulty, getAlarmDiffPos(alarm));
    }

    public static String getAlarmPuzzleKey(Context context, Alarm alarm) {
        return getArrayValueAt(context, R.array.pref_puzzle_values, getAlarmPuzzlePos(alarm));
    }

    public static String getAlarmPuzzleTitle(Context context, Alarm alarm) {
        return getArrayValueAt(context, R.array.pref_puzzle, getAlarmPuzzlePos(alarm));
    }
}
