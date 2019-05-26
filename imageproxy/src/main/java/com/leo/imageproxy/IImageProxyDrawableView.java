package com.leo.imageproxy;

import android.graphics.drawable.Drawable;

/**
 * 自定义view实现
 */
public interface IImageProxyDrawableView {
    void setDrawable(Drawable drawable);

    int getWidth();

    int getHeight();
}
