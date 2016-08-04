package com.domain.nvm.morningfriend.database;

import android.provider.BaseColumns;

public class AlarmContract {
    public static final class AlarmsTable {
        public static final String NAME = "alarms";

        public static final class Cols implements BaseColumns {
            public static final String TIME = "time";
            public static final String ENABLED = "is_enabled";
            public static final String RECURRING = "is_recurring";
            public static final String PUZZLE = "puzzle";
            public static final String DIFFICULTY = "difficulty";
        }

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + NAME + "(" +
                Cols._ID + " integer primary key autoincrement, " +
                Cols.TIME + ", " +
                Cols.ENABLED + ", " +
                Cols.RECURRING + ", " +
                Cols.PUZZLE + ", " +
                Cols.DIFFICULTY + ")";
    }
}
