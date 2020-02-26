package com.leo.imageproxy

import android.graphics.drawable.Drawable

/**
 * 自定义view实现
 */
interface IImgProxyDrawableView {

    val imgWidth: Int

    val imgHeight: Int
    fun loadDrawable(drawable: Drawable)
}
