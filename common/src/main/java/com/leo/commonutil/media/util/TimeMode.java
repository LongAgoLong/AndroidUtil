package com.leo.commonutil.media.util;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 自定义一个注解Mode
@StringDef({TimeMode.MODE_SECOND,
        TimeMode.MODE_FORMAT})
@Retention(RetentionPolicy.SOURCE)
public @interface TimeMode {
    String MODE_SECOND = "SECOND";//转换成秒
    String MODE_FORMAT = "FORMAT";//格式化
}
