package com.leo.system.enume;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({LogType.VERBOSE,
        LogType.DEBUG,
        LogType.INFO,
        LogType.WARN,
        LogType.ERROR,
        LogType.ASSERT})
@Target({ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface LogType {
    /**
     * Priority constant for the println method; use Log.v.
     */
    int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    int ASSERT = 7;
}
