package com.leo.commonutil.enumerate;

import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;

@IntDef({LogType.RELEASE,
        LogType.DEBUG})
public @interface LogType {
    int RELEASE = 0;
    int DEBUG = 1;
}
