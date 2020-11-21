package com.leo.commonutil.media.mp3;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({Mp3PlayState.NONE, Mp3PlayState.PLAY, Mp3PlayState.PAUSE,
        Mp3PlayState.STOP})
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface Mp3PlayState {
    int NONE = 0; // 默认状态
    int PLAY = 1; // 播放
    int PAUSE = 2; // 暂停
    int STOP = 3; // 停止播放
}
