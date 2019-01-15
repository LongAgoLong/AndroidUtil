package com.leo.commonutil.media.util;

import android.support.annotation.StringDef;

// 自定义一个注解Mode
@StringDef({TimeMode.MODE_SECOND,
        TimeMode.MODE_FORMAT})
public @interface TimeMode {
    String MODE_SECOND = "SECOND";//转换成秒
    String MODE_FORMAT = "FORMAT";//格式化
}
