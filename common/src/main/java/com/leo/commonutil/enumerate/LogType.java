package com.leo.commonutil.enumerate;

import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({LogType.RELEASE,
        LogType.ABTEST,
        LogType.DEBUG})
@Retention(RetentionPolicy.SOURCE)
public @interface LogType {
    int RELEASE = 0;
    int ABTEST = 1;
    int DEBUG = 2;
}
