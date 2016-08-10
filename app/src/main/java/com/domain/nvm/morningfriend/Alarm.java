package com.domain.nvm.morningfriend;

import java.io.Serializable;
import java.util.Date;

public class Alarm implements Serializable {

    public enum Difficulty {EASY, MEDIUM, HARD}
    public enum Puzzle {SQUARES, GRAPH}

    private int id;
    private int hour;
    private int minute;
    private long time;
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

    public long getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time.getTime();
    }

    public void setTime(long time) {
        this.time = time;
    }

}
