package com.leo.imageproxy.enume;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        ImageTransType.COLOR_FILTER,
        ImageTransType.GRAY_SCALE,
        ImageTransType.BLUR,
        ImageTransType.SUPPORT_RS_BLUR,
        ImageTransType.TOON,
        ImageTransType.SEPIA,
        ImageTransType.CONTRAST,
        ImageTransType.INVERT,
        ImageTransType.PIXEL,
        ImageTransType.SKETCH,
        ImageTransType.SWIRL,
        ImageTransType.BRIGHTNESS,
        ImageTransType.KUAWAHARA,
        ImageTransType.VIGNETTE
})
@Retention(RetentionPolicy.SOURCE)
public @interface ImageTransType {
    // 颜色转换
    int COLOR_FILTER = 0;
    // 灰度转换
    int GRAY_SCALE = 1;
    // 高斯模糊
    int BLUR = 2;
    // RS高斯模糊
    int SUPPORT_RS_BLUR = 3;
    // 卡通滤波
    int TOON = 4;
    // 乌墨色滤波
    int SEPIA = 5;
    // 对比度滤波
    int CONTRAST = 6;
    // 反转滤波
    int INVERT = 7;
    // 像素化滤波
    int PIXEL = 8;
    // 素描滤波
    int SKETCH = 9;
    // 旋转滤波
    int SWIRL = 10;
    // 亮度滤波
    int BRIGHTNESS = 11;
    // Kuwahara滤波
    int KUAWAHARA = 12;
    // 装饰图滤波
    int VIGNETTE = 13;
}
