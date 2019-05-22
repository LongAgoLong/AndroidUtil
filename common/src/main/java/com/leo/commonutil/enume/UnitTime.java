package com.leo.commonutil.enume;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({UnitTime.HOUR,
        UnitTime.MINUTE,
        UnitTime.SECOND,
        UnitTime.MILLIONSECOND})
@Retention(RetentionPolicy.SOURCE)
public @interface UnitTime {
    int HOUR = 0;
    int MINUTE = 1;
    int SECOND = 2;
    int MILLIONSECOND = 3;
}