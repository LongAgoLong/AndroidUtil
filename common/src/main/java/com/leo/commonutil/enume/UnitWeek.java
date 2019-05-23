package com.leo.commonutil.enume;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        UnitWeek.SUNDAY,
        UnitWeek.MONDAY,
        UnitWeek.TUESDAY,
        UnitWeek.WEDNESDAY,
        UnitWeek.THURSDAY,
        UnitWeek.FRIDAY,
        UnitWeek.SATURDAY,
})
@Retention(RetentionPolicy.SOURCE)
public @interface UnitWeek {
    int SUNDAY = 0;
    int MONDAY = 1;
    int TUESDAY = 2;
    int WEDNESDAY = 3;
    int THURSDAY = 4;
    int FRIDAY = 5;
    int SATURDAY = 6;
}
