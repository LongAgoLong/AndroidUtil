package com.leo.commonutil.enumerate;

import android.support.annotation.IntDef;

@IntDef({UnitTime.HOUR,
        UnitTime.MINUTE,
        UnitTime.SECOND,
        UnitTime.MILLIONSECOND})
public @interface UnitTime {
    int HOUR = 0;
    int MINUTE = 1;
    int SECOND = 2;
    int MILLIONSECOND = 3;
}