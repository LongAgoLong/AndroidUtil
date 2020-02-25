package com.leo.imageproxy

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import java.io.File

interface IImgProxy {
    /**
     * 加载磁盘中缓存图片文件
     */
    fun getDiskFile(context: Context, url: String): File

    /**
     * 可改变形状的加载方式
     *
     * @param context
     * @param mode
     * @param drawId
     * @param url
     * @param isBitmap
     * @param imageView
     */
    fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String, isBitmap: Boolean,
                  imageView: ImageView)

    fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                  bitmapView: IImgProxyBitmapView)

    fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                  drawableView: IImgProxyDrawableView)

    /**
     * 圆角加载方式
     *
     * @param context
     * @param url
     * @param isBitmap
     * @param px
     * @param imageView
     */
    fun loadCircularBeadImage(context: Context, url: String, isBitmap: Boolean, px: Int, imageView: ImageView)

    fun loadCircularBeadImage(context: Context, url: String, px: Int, bitmapView: IImgProxyBitmapView)

    fun loadCircularBeadImage(context: Context, url: String, px: Int, drawableView: IImgProxyDrawableView)

    /**
     * 提供变换的加载方式
     *
     * @param context
     * @param url
     * @param transType link ImgTransType
     * @param value
     */
    fun loadTransImage(context: Context, url: String, transType: Int, imageView: ImageView, vararg value: Float)

    fun loadTransImage(context: Context, url: String, transType: Int, bitmapView: IImgProxyBitmapView, vararg value: Float)

    fun loadTransImage(context: Context, url: String, transType: Int, drawableView: IImgProxyDrawableView, vararg value: Float)
}
