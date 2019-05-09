package com.leo.commonutil.enumerate;

import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({LogType.RELEASE,
        LogType.DEBUG})
@Retention(RetentionPolicy.SOURCE)
public @interface LogType {
    int RELEASE = 0;
    int DEBUG = 1;
}
