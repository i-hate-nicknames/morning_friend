package com.domain.nvm.morningfriend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.domain.nvm.morningfriend.alert.AlertReceiver;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Alarm implements Serializable {

    public enum Difficulty {EASY, MEDIUM, HARD}
    public enum Puzzle {SQUARES, GRAPH}

    private int id;
    private int hour;
    private int minute;
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

    public static Alarm emptyAlarm() {
        Alarm a = new Alarm();
        a.setTime(new Date());
        a.setEnabled(false);
        a.setDifficulty(Difficulty.EASY);
        a.setPuzzle(Puzzle.SQUARES);
        return a;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
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
        this.time = Utils.createAlarmTime(time);
    }

    public void schedule(Context context, boolean isOn) {
        /*Intent i = AlertReceiver.newIntent(context, time);
        PendingIntent pi =
                PendingIntent.getBroadcast(context, 0, i, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            am.set(AlarmManager.RTC_WAKEUP, time.getTime(), pi);
        }
        else {
            am.cancel(pi);
            pi.cancel();
        }*/
    }

    public void setNextAlarm(Context context) {
        /*this.time = Utils.createAlarmTime(this.time);
        schedule(context, true);*/
    }


}
