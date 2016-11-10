package com.domain.nvm.morningfriend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alarm implements Serializable {

    private static final long DEFAULT_SNOOZE_DURATION = 5 * 1000;

    public enum Difficulty {EASY, MEDIUM, HARD}
    public enum PuzzleType {SQUARES, GRAPH}

    private int id;
    private int hour;
    private int minute;
    private long time;
    private long snoozeDuration;
    private boolean isSnooze;
    private String message;
    private boolean isEnabled;
    private Difficulty difficulty;
    private PuzzleType mPuzzleType;
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
        a.setPuzzleType(PuzzleType.SQUARES);
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

    public PuzzleType getPuzzleType() {
        return mPuzzleType;
    }

    public void setPuzzleType(PuzzleType puzzleType) {
        this.mPuzzleType = puzzleType;
    }

    public void setPuzzleType(int puzzleTypePos) {
        try {
            this.mPuzzleType = PuzzleType.values()[puzzleTypePos];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new InvalidPuzzleException(Integer.toString(puzzleTypePos));
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

        private static Days sWeekends = new Days();
        private static Days sWorkdays = new Days();
        static {
            sWeekends.setDay(Names.SAT, true);
            sWeekends.setDay(Names.SUN, true);
            sWorkdays.setDay(Names.MON, true);
            sWorkdays.setDay(Names.TUE, true);
            sWorkdays.setDay(Names.WED, true);
            sWorkdays.setDay(Names.THU, true);
            sWorkdays.setDay(Names.FRI, true);
        }

        private int bitMask;

        public Days() {
            this.bitMask = 0;
        }

        public Days(int bitMask) {
            this.bitMask = bitMask;
        }

        public int getBitMask() {
            return bitMask;
        }

        public boolean isDayActive(Names day) {
            return ((bitMask>>day.ordinal()) % 2) == 1;
        }

        public List<Names> getActiveDays() {
            List<Names> names = new ArrayList<>();
            int offset = 0;
            while (bitMask>>offset != 0) {
                if ((bitMask>>offset) % 2 == 1) {
                    names.add(Names.values()[offset]);
                }
                offset++;
            }
            return names;
        }

        public int getClosestDayIndex(int startDay) {
            if (bitMask == 0) {
                return -1;
            }
            int offset = 0;
            // shift until the end of mask
            while (offset+startDay <= Names.SAT.ordinal()) {
                if ((bitMask>>offset+startDay) % 2 == 1) {
                    return offset;
                }
                offset++;
            }
            // at this point we have shifted all the way left to sat and our current offset value
            // is the number of shifts from startDay to the end of mask
            // (also a number of days from startDay to Sunday). Since we haven't found
            // active days yet, we have to take number of days until sunday (offset) and add to it
            // position of the first active day starting from Sunday
            return offset+getClosestDayIndex(0);
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
            this.bitMask = sWeekends.bitMask;

        }

        public void setOnlyWorkDays() {
            this.bitMask = sWorkdays.bitMask;

        }

        public boolean isOnlyWeekends() {
            return this.bitMask == sWeekends.bitMask;
        }

        public boolean isEveryDay() {
            return this.bitMask == (sWeekends.bitMask + sWorkdays.bitMask);
        }

        public boolean isOnlyWorkDays() {
            return this.bitMask == sWorkdays.bitMask;
        }

        public static boolean isDayWeekend(int dayCode) {
            Names day = Names.values()[dayCode];
            return day == Names.SAT || day == Names.SUN;
        }

        public static boolean isDayWorkday(int dayCode) {
            return !isDayWeekend(dayCode);
        }
    }


}
