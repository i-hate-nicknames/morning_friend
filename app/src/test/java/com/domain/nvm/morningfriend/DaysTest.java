package com.domain.nvm.morningfriend;

import com.domain.nvm.morningfriend.Alarm.Days.Names;

import org.junit.Test;

import static org.junit.Assert.*;

public class DaysTest {

    @Test
    public void testSetDay() {
        Alarm a = Alarm.emptyAlarm();
        Alarm.Days repeatDays = a.getRepeatDays();
        assertFalse(repeatDays.isDayActive(Names.MON));
        repeatDays.setDay(Names.MON, true);
        assertTrue(repeatDays.isDayActive(Names.MON));
        assertFalse(repeatDays.isDayActive(Names.TUE));
    }

    @Test
    public void testSetGroup() {
        Alarm a = Alarm.emptyAlarm();
        Alarm.Days repeatDays = a.getRepeatDays();
        assertFalse(repeatDays.isDayActive(Names.MON));
        assertFalse(repeatDays.isDayActive(Names.SUN));
        repeatDays.setOnlyWorkDays();
        assertTrue(repeatDays.isDayActive(Names.MON));
        assertTrue(repeatDays.isDayActive(Names.TUE));
        assertTrue(repeatDays.isDayActive(Names.WED));
        assertTrue(repeatDays.isDayActive(Names.THU));
        assertTrue(repeatDays.isDayActive(Names.FRI));
        assertFalse(repeatDays.isDayActive(Names.SAT));
        assertFalse(repeatDays.isDayActive(Names.SUN));
        assertTrue(repeatDays.isOnlyWorkDays());
        assertFalse(repeatDays.isOnlyWeekends());

        repeatDays.setOnlyWeekends();
        assertFalse(repeatDays.isDayActive(Names.MON));
        assertFalse(repeatDays.isDayActive(Names.TUE));
        assertFalse(repeatDays.isDayActive(Names.WED));
        assertFalse(repeatDays.isDayActive(Names.THU));
        assertFalse(repeatDays.isDayActive(Names.FRI));
        assertTrue(repeatDays.isDayActive(Names.SAT));
        assertTrue(repeatDays.isDayActive(Names.SUN));
        assertTrue(repeatDays.isOnlyWeekends());
        assertFalse(repeatDays.isOnlyWorkDays());
    }
}
