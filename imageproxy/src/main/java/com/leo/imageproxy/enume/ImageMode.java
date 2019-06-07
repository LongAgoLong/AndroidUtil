package com.leo.imageproxy.enume;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ImageMode.NORMAL,
        ImageMode.CIRCULAR,
        ImageMode.SQUARE,
        ImageMode.MASK,
        ImageMode.NINE_PATCH_MASK,
        ImageMode.OTHER,
        ImageMode.OTHER2,
        ImageMode.OTHER3})
@Retention(RetentionPolicy.SOURCE)
public @interface ImageMode {
    // 普通默认
    int NORMAL = 0;
    // 圆形
    int CIRCULAR = 1;
    // 正方形
    int SQUARE = 2;
    int MASK = 3;
    int NINE_PATCH_MASK = 4;
    int OTHER = 5;
    int OTHER2 = 6;
    int OTHER3 = 7;
}
