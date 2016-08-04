package com.domain.nvm.morningfriend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.database.AlarmsContract.AlarmsTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmsRepository {

    private static AlarmsRepository sInstance;
    private static Context sContext;

    private SQLiteDatabase mDatabase;

    public static AlarmsRepository get(Context context) {
        if (sInstance == null) {
            sInstance = new AlarmsRepository(context);
        }
        return sInstance;
    }

    public AlarmsRepository(Context context) {
        sContext = context.getApplicationContext();
        mDatabase = new AlarmBaseHelper(sContext).getWritableDatabase();
    }

    public List<Alarm> getAlarms() {
        AlarmsWrapper cursor = queryAlarms(null, null);
        List<Alarm> alarms = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                alarms.add(cursor.getAlarm());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return alarms;
    }

    public Alarm addAlarm() {
        Alarm a = Alarm.emptyAlarm();
        long id = mDatabase.insert(AlarmsTable.NAME, null, getContentValues(a));
        a.setId((int)id);
        return a;
    }

    public void updateAlarm(Alarm a) {
        mDatabase.update(AlarmsTable.NAME, getContentValues(a),
                " _id = ?", new String[] {Integer.toString(a.getId())});
    }

    public void deleteAlarm(Alarm a) {
        mDatabase.delete(AlarmsTable.NAME, " _id = ?",
                new String[] {Integer.toString(a.getId())});
    }

    public Alarm getAlarm(int id) {
        AlarmsWrapper cursor = queryAlarms(" _id = ?", new String[] {Integer.toString(id)});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getAlarm();
        }
        finally {
            cursor.close();
        }

    }

    private AlarmsWrapper queryAlarms(String selection, String[] selArgs) {
        Cursor c = mDatabase.query(AlarmsTable.NAME, null, selection, selArgs, null, null, null);
        return new AlarmsWrapper(c);
    }

    private ContentValues getContentValues(Alarm a) {
        ContentValues vals = new ContentValues();
        vals.put(AlarmsTable.Cols.TIME, a.getTime().getTime());
        vals.put(AlarmsTable.Cols.DIFFICULTY, a.getDifficulty().ordinal());
        vals.put(AlarmsTable.Cols.PUZZLE, a.getPuzzle().ordinal());
        vals.put(AlarmsTable.Cols.ENABLED, a.isEnabled() ? 1 : 0);
        return vals;
    }
}
