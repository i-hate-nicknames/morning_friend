package com.domain.nvm.morningfriend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.database.AlarmsContract.AlarmsTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmsRepository {

    private static AlarmsRepository sInstance;
    private static Context sContext;

    private AlarmBaseHelper dbHelper;

    public static AlarmsRepository get(Context context) {
        if (sInstance == null) {
            sInstance = new AlarmsRepository(context);
        }
        return sInstance;
    }

    public AlarmsRepository(Context context) {
        sContext = context.getApplicationContext();
        dbHelper = new AlarmBaseHelper(sContext);
    }

    public List<Alarm> getAlarms() {
        Cursor c = dbHelper.getReadableDatabase().query(
                AlarmsTable.NAME, null, null, null, null, null, null);
        AlarmsWrapper cursor = new AlarmsWrapper(c);
        List<Alarm> alarms = new ArrayList<>();
        while (cursor.moveToNext()) {
            alarms.add(cursor.getAlarm());
        }
        return alarms;
    }

    public Alarm addAlarm() {
        Alarm a = Alarm.emptyAlarm();
        long id = dbHelper.getWritableDatabase()
                .insert(AlarmsTable.NAME, null, getContentValues(a));
        a.setId((int)id);
        return a;
    }

    public void updateAlarm(Alarm a) {
        dbHelper.getWritableDatabase()
                .update(AlarmsTable.NAME, getContentValues(a),
                        " _id = ?", new String[] {Integer.toString(a.getId())});
    }

    public void deleteAlarm(Alarm a) {
        dbHelper.getWritableDatabase()
                .delete(AlarmsTable.NAME, " _id = ?",
                        new String[] {Integer.toString(a.getId())});
    }

    public Alarm getAlarm(int id) {
        Cursor c = dbHelper.getReadableDatabase().query(
                AlarmsTable.NAME, null,
                " _id = ?", new String[] {Integer.toString(id)},
                null, null, null);
        AlarmsWrapper cursor = new AlarmsWrapper(c);
        if (cursor.moveToFirst()) {
            return cursor.getAlarm();
        }
        return null;
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
