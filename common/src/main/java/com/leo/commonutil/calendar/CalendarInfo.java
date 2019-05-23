package com.leo.commonutil.calendar;

import com.leo.commonutil.callback.IProguard;
import com.leo.commonutil.enume.UnitWeek;

public class CalendarInfo implements IProguard {
    public int year;
    public int month;
    public int day;
    @UnitWeek
    public int week;

    public CalendarInfo(int year, int month, int day, @UnitWeek int week) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
    }
}
