package com.leo.commonutil.enume;

import android.support.annotation.IntDef;

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
