package com.leo.imageproxy

import android.graphics.Bitmap

/**
 * 自定义view实现
 */
interface IImgProxyBitmapView {

    val width: Int

    val height: Int
    fun setBitmap(bitmap: Bitmap)
}
