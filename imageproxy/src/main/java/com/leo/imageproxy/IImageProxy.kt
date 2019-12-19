package com.leo.imageproxy

import android.content.Context
import android.widget.ImageView

import androidx.annotation.DrawableRes

interface IImageProxy {

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
                  bitmapView: IImageProxyBitmapView)

    fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                  drawableView: IImageProxyDrawableView)

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

    fun loadCircularBeadImage(context: Context, url: String, px: Int, bitmapView: IImageProxyBitmapView)

    fun loadCircularBeadImage(context: Context, url: String, px: Int, drawableView: IImageProxyDrawableView)

    /**
     * 提供变换的加载方式
     *
     * @param context
     * @param url
     * @param transType link ImageTransType
     * @param value
     */
    fun loadTransImage(context: Context, url: String, transType: Int, vararg value: Float)
}
