package com.leo.imageproxy

import android.graphics.Bitmap

/**
 * 自定义view实现
 */
interface IImgProxyBitmapView {

    val imgWidth: Int

    val imgHeight: Int
    fun loadBitmap(bitmap: Bitmap)
}
