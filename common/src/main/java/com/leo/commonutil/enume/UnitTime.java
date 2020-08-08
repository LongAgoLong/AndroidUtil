package com.leo.commonutil.enume;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({UnitTime.HOUR,
        UnitTime.MINUTE,
        UnitTime.SECOND,
        UnitTime.MILLIONSECOND})
@Target({ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface UnitTime {
    int HOUR = 0;
    int MINUTE = 1;
    int SECOND = 2;
    int MILLIONSECOND = 3;
}