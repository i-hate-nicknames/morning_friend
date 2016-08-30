package com.domain.nvm.morningfriend.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.database.AlarmContract.AlarmsTable;

import java.util.Date;

public class AlarmCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public AlarmCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Alarm getAlarm() {
        int id = getInt(getColumnIndex(AlarmsTable.Cols._ID));
        int hour = getInt(getColumnIndex(AlarmsTable.Cols.HOUR));
        int minute = getInt(getColumnIndex(AlarmsTable.Cols.MINUTE));
        String message = getString(getColumnIndex(AlarmsTable.Cols.MESSAGE));
        boolean isEnabled = getInt(getColumnIndex(AlarmsTable.Cols.ENABLED)) == 1;
        int puzzleId = getInt(getColumnIndex(AlarmsTable.Cols.PUZZLE));
        int diffictultyId = getInt(getColumnIndex(AlarmsTable.Cols.DIFFICULTY));
        int repeatingDaysMask = getInt(getColumnIndex(AlarmsTable.Cols.REPEAT_DAYS));
        Alarm a = Alarm.emptyAlarm();
        a.setId(id);
        a.setHour(hour);
        a.setMinute(minute);
        a.setMessage(message);
        a.setEnabled(isEnabled);
        a.setPuzzle(puzzleId);
        a.setDifficulty(diffictultyId);
        a.setRepeatDays(new Alarm.Days(repeatingDaysMask));
        return a;
    }
}
