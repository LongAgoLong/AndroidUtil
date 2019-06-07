package com.leo.commonutil.enume;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({CalendarAddResult.SUCCESS,
        CalendarAddResult.NO_USER_ID,
        CalendarAddResult.NO_EVENT_URL,
        CalendarAddResult.OTHER_ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface CalendarAddResult {
    int SUCCESS = 0;
    int NO_USER_ID = 1;
    int NO_EVENT_URL = 2;
    int OTHER_ERROR = 3;
}
