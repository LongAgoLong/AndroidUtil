package com.leo.commonutil.enumerate;

import android.support.annotation.IntDef;

@IntDef({
        NetType.WIFI,
        NetType.LTE,
        NetType.EDGE,
        NetType.NONE
})
public @interface NetType {
    int WIFI = 0;
    int LTE = 1;
    int EDGE = 2;
    int NONE = 3;
}
