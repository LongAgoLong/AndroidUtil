package com.leo.commonutil.enumerate;

import android.support.annotation.IntDef;

@IntDef({CalendarAddResult.SUCCESS,
        CalendarAddResult.NO_USER_ID,
        CalendarAddResult.NO_EVENT_URL,
        CalendarAddResult.OTHER_ERROR})
public @interface CalendarAddResult {
    int SUCCESS = 0;
    int NO_USER_ID = 1;
    int NO_EVENT_URL = 2;
    int OTHER_ERROR = 3;
}
