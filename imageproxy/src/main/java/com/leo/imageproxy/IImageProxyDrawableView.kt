package com.leo.imageproxy

import android.graphics.drawable.Drawable

/**
 * 自定义view实现
 */
interface IImageProxyDrawableView {

    val width: Int

    val height: Int
    fun setDrawable(drawable: Drawable)
}
