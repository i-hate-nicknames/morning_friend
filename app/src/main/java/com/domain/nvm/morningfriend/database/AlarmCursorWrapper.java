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
        long time = getLong(getColumnIndex(AlarmsTable.Cols.TIME));
        boolean isEnabled = getInt(getColumnIndex(AlarmsTable.Cols.ENABLED)) == 1;
        int puzzleId = getInt(getColumnIndex(AlarmsTable.Cols.PUZZLE));
        int diffictultyId = getInt(getColumnIndex(AlarmsTable.Cols.DIFFICULTY));
        Alarm a = Alarm.emptyAlarm();
        a.setId(id);
        a.setTime(new Date(time));
        a.setEnabled(isEnabled);
        a.setPuzzle(puzzleId);
        a.setDifficulty(diffictultyId);
        return a;
    }
}
