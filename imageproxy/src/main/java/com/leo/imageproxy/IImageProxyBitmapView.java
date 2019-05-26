package com.leo.imageproxy;

import android.graphics.Bitmap;

/**
 * 自定义view实现
 */
public interface IImageProxyBitmapView {
    void setBitmap(Bitmap bitmap);

    int getWidth();

    int getHeight();
}
