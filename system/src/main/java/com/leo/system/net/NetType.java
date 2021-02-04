package com.leo.system.net;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({
        NetType.WIFI,
        NetType.LTE,
        NetType.EDGE,
        NetType.NONE
})
@Target({ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface NetType {
    int WIFI = 0;
    int LTE = 1;
    int EDGE = 2;
    int NONE = 3;
}
