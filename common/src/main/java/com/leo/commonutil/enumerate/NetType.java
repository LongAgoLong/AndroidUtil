package com.leo.commonutil.enumerate;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        NetType.WIFI,
        NetType.LTE,
        NetType.EDGE,
        NetType.NONE
})
@Retention(RetentionPolicy.SOURCE)
public @interface NetType {
    int WIFI = 0;
    int LTE = 1;
    int EDGE = 2;
    int NONE = 3;
}
