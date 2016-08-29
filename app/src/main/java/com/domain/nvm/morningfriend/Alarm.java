package com.domain.nvm.morningfriend;

import java.io.Serializable;
import java.util.Date;

public class Alarm implements Serializable {

    private static final long DEFAULT_SNOOZE_DURATION = 5 * 1000;

    public enum Difficulty {EASY, MEDIUM, HARD}
    public enum Puzzle {SQUARES, GRAPH}

    private int id;
    private int hour;
    private int minute;
    private long time;
    private long snoozeDuration;
    private boolean isSnooze;
    private String message;
    private boolean isEnabled;
    private Difficulty difficulty;
    private Puzzle puzzle;
    private Days repeatDays;

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
        a.repeatDays = new Days();
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

    public long getSnoozeDuration() {
        if (snoozeDuration == 0) {
            return DEFAULT_SNOOZE_DURATION;
        }
        return snoozeDuration;
    }

    public boolean isSnooze() {
        return isSnooze;
    }

    public void setSnooze(boolean snooze) {
        isSnooze = snooze;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Days getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(Days repeatDays) {
        this.repeatDays = repeatDays;
    }

    /**
     * Repeating days are represented as a bit mask, where every bit from 0th to 6th denote
     * repeating at a particular day of week, starting from Sunday at 0th bit.
     */
    public static class Days implements Serializable {

        public enum Names { SUN, MON, TUE, WED, THU, FRI, SAT}

        private int bitMask;

        public boolean isDayActive(Names day) {
            return ((bitMask>>day.ordinal()) % 2) == 1;
        }

        public void setDay(Names day, boolean enable) {
            int dayMask = 1<<day.ordinal();
            if (enable) {
                this.bitMask |= dayMask;
            }
            else {
                this.bitMask &= ~dayMask;
            }
        }

        public void setOnlyWeekends() {
            this.bitMask = 0;
            setDay(Names.SAT, true);
            setDay(Names.SUN, true);
        }

        public void setOnlyWorkDays() {
            this.bitMask = 0;
            setDay(Names.MON, true);
            setDay(Names.TUE, true);
            setDay(Names.WED, true);
            setDay(Names.THU, true);
            setDay(Names.FRI, true);
        }

    }


}
