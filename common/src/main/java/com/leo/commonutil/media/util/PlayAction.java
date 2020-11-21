package com.leo.commonutil.media.util;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({PlayAction.SPEAKER,
        PlayAction.RECEIVER,
        PlayAction.HEADSET})
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface PlayAction {
    int SPEAKER = 0; // 外放
    int RECEIVER = 1; // 听筒
    int HEADSET = 2; // 耳机
}
