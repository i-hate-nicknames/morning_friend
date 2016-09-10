package com.domain.nvm.morningfriend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.Utils;
import com.domain.nvm.morningfriend.database.AlarmContract.AlarmsTable;
import com.domain.nvm.morningfriend.alert.scheduler.AlarmScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AlarmRepository {

    private static AlarmRepository sInstance;
    private static Context sContext;

    private SQLiteDatabase mDatabase;

    public static AlarmRepository get(Context context) {
        if (sInstance == null) {
            sInstance = new AlarmRepository(context);
        }
        return sInstance;
    }

    public AlarmRepository(Context context) {
        sContext = context.getApplicationContext();
        mDatabase = new AlarmBaseHelper(sContext).getWritableDatabase();
    }

    public List<Alarm> getAlarms() {
        AlarmCursorWrapper cursor = queryAlarms(null, null);
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

    public List<Alarm> getSortedAlarms() {
        List<Alarm> unsorted = getAlarms();
        Alarm[] alarms = unsorted.toArray(new Alarm[unsorted.size()]);
        Arrays.sort(alarms, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm lhs, Alarm rhs) {
                return (int) (Utils.calculateAlertTime(lhs).getTime() -
                        Utils.calculateAlertTime(rhs).getTime());
            }
        });
        Arrays.sort(alarms, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm lhs, Alarm rhs) {
                int lVal = lhs.isEnabled() ? 1 : 0;
                int rVal = rhs.isEnabled() ? 1 : 0;
                return rVal - lVal;
            }
        });
        return Arrays.asList(alarms);
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
        AlarmScheduler.setNextAlarm(sContext);
    }

    public void deleteAlarm(Alarm a) {
        mDatabase.delete(AlarmsTable.NAME, " _id = ?",
                new String[] {Integer.toString(a.getId())});
        AlarmScheduler.setNextAlarm(sContext);
    }

    public Alarm getAlarm(int id) {
        AlarmCursorWrapper cursor = queryAlarms(" _id = ?", new String[] {Integer.toString(id)});
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

    private AlarmCursorWrapper queryAlarms(String selection, String[] selArgs) {
        Cursor c = mDatabase.query(AlarmsTable.NAME, null, selection, selArgs, null, null, null);
        return new AlarmCursorWrapper(c);
    }

    private ContentValues getContentValues(Alarm a) {
        ContentValues vals = new ContentValues();
        vals.put(AlarmsTable.Cols.HOUR, a.getHour());
        vals.put(AlarmsTable.Cols.MINUTE, a.getMinute());
        vals.put(AlarmsTable.Cols.MESSAGE, a.getMessage());
        vals.put(AlarmsTable.Cols.DIFFICULTY, a.getDifficulty().ordinal());
        vals.put(AlarmsTable.Cols.PUZZLE, a.getPuzzle().ordinal());
        vals.put(AlarmsTable.Cols.ENABLED, a.isEnabled() ? 1 : 0);
        vals.put(AlarmsTable.Cols.REPEAT_DAYS, a.getRepeatDays().getBitMask());
        return vals;
    }
}
