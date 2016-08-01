package com.domain.nvm.morningfriend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.domain.nvm.morningfriend.alert.AlertReceiver;

import java.util.Calendar;
import java.util.Date;

public class Alarm {

    public enum Difficulty {EASY, MEDIUM, HARD}
    public enum Puzzle {SQUARES, GRAPH}

    private int id;
    private Date time;
    private boolean isEnabled;
    private Difficulty difficulty;
    private Puzzle puzzle;

    public static class InvalidDifficultyException extends IllegalArgumentException {
        public InvalidDifficultyException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class InvalidPuzzleException extends IllegalArgumentException {
        public InvalidPuzzleException(String detailMessage) {
            super(detailMessage);
        }
    }

    public Alarm(int id, Date time) {
        this.id = id;
        this.time = time;
        difficulty = Difficulty.EASY;
        puzzle = Puzzle.SQUARES;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDifficulty(int difficultyPos) {
        try {
            this.difficulty = Difficulty.values()[difficultyPos];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new InvalidDifficultyException(Integer.toString(difficultyPos));
        }
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void setPuzzle(int puzzlePos) {
        try {
            this.puzzle = Puzzle.values()[puzzlePos];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new InvalidPuzzleException(Integer.toString(puzzlePos));
        }
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = validateTime(time);
    }

    public void schedule(Context context, boolean isOn) {
        Intent i = AlertReceiver.newIntent(context, time);
        PendingIntent pi =
                PendingIntent.getBroadcast(context, 0, i, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            am.set(AlarmManager.RTC_WAKEUP, time.getTime(), pi);
        }
        else {
            am.cancel(pi);
            pi.cancel();
        }
    }

    public void setNextAlarm(Context context) {
        this.time = validateTime(this.time);
        schedule(context, true);
    }

    private Date validateTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.SECOND, 0);
        if (time.getTime() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTime();
    }
}