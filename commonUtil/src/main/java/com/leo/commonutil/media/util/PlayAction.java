package com.leo.commonutil.media.util;

import android.support.annotation.IntDef;

@IntDef({PlayAction.SPEAKER,
        PlayAction.RECEIVER,
        PlayAction.HEADSET})
public @interface PlayAction {
    int SPEAKER = 0;//外放
    int RECEIVER = 1;//听筒
    int HEADSET = 2;//耳机
}
