package com.domain.nvm.morningfriend.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.database.AlarmsContract.AlarmsTable;

public class AlarmsWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public AlarmsWrapper(Cursor cursor) {
        super(cursor);
    }

    public Alarm getAlarm() {
        int id = getInt(getColumnIndex(AlarmsTable.Cols._ID));
        // todo: get all other fields, create alarm object and return it
        throw new UnsupportedOperationException();
    }
}
