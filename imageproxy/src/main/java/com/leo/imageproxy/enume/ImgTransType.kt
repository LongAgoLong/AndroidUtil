package com.leo.imageproxy.enume

object ImgTransType {
    // 颜色转换
    const val COLOR_FILTER = 0
    // 灰度转换
    const val GRAY_SCALE = 1
    // 高斯模糊
    const val BLUR = 2
    // RS高斯模糊
    const val SUPPORT_RS_BLUR = 3
    // 卡通滤波
    const val TOON = 4
    // 乌墨色滤波
    const val SEPIA = 5
    // 对比度滤波
    const val CONTRAST = 6
    // 反转滤波
    const val INVERT = 7
    // 像素化滤波
    const val PIXEL = 8
    // 素描滤波
    const val SKETCH = 9
    // 旋转滤波
    const val SWIRL = 10
    // 亮度滤波
    const val BRIGHTNESS = 11
    // Kuwahara滤波
    const val KUAWAHARA = 12
    // 装饰图滤波
    const val VIGNETTE = 13
}
