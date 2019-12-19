package com.leo.imageproxy

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.leo.imageproxy.enume.ImageMode

/**
 * 对外调用方法
 */
class ImageProxyHelp private constructor() {
    private var proxyImpl: IImageProxy? = null

    /**
     * 设置代理实现
     * 只第一次设置有效
     *
     * @param proxyImpl
     */
    fun setProxyImpl(proxyImpl: IImageProxy?) {
        this.proxyImpl ?: synchronized(this) {
            this.proxyImpl ?: proxyImpl.also { this.proxyImpl = it }
        }
    }

    private fun getProxyImpl(): IImageProxy {
        proxyImpl ?: throw RuntimeException("ImageProxyHelp : must setProxyImpl in application first")
        return proxyImpl as IImageProxy
    }

    /**
     * 普通默认加载
     *
     * @param context
     * @param url
     * @param isBitmap
     * @param imageView
     */
    fun loadImage(context: Context, url: String, isBitmap: Boolean, imageView: ImageView) {
        getProxyImpl().loadImage(context, ImageMode.NORMAL, R.color.place_holder_color, url, isBitmap, imageView)
    }

    fun loadImage(context: Context, url: String, bitmapView: IImageProxyBitmapView) {
        getProxyImpl().loadImage(context, ImageMode.NORMAL, R.color.place_holder_color, url, bitmapView)
    }

    fun loadImage(context: Context, url: String, drawableView: IImageProxyDrawableView) {
        getProxyImpl().loadImage(context, ImageMode.NORMAL, R.color.place_holder_color, url, drawableView)
    }

    /**
     * 提供普通/圆形/正方形/Mask/NinePatchMask加载方式
     * 也提供ImageMode.OTHER,ImageMode.OTHER2,ImageMode.OTHER3三种自定义实现
     *
     * @param context
     * @param mode
     * @param drawId
     * @param url
     * @param isBitmap
     * @param imageView
     */
    fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String, isBitmap: Boolean,
                  imageView: ImageView) {
        getProxyImpl().loadImage(context, mode, drawId, url, isBitmap, imageView)
    }

    fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                  bitmapView: IImageProxyBitmapView) {
        getProxyImpl().loadImage(context, mode, drawId, url, bitmapView)
    }

    fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                  drawableView: IImageProxyDrawableView) {
        getProxyImpl().loadImage(context, mode, drawId, url, drawableView)
    }

    /**
     * 提供圆角加载方式
     *
     * @param context
     * @param url
     * @param isBitmap
     * @param px
     * @param imageView
     */
    fun loadCircularBeadImage(context: Context, url: String, isBitmap: Boolean, px: Int,
                              imageView: ImageView) {
        getProxyImpl().loadCircularBeadImage(context, url, isBitmap, px, imageView)
    }

    fun loadCircularBeadImage(context: Context, url: String, px: Int, bitmapView: IImageProxyBitmapView) {
        getProxyImpl().loadCircularBeadImage(context, url, px, bitmapView)
    }

    fun loadCircularBeadImage(context: Context, url: String, px: Int,
                              drawableView: IImageProxyDrawableView) {
        getProxyImpl().loadCircularBeadImage(context, url, px, drawableView)
    }

    /**
     *
     * @param context
     * @param url
     * @param transType link ImageTransType
     * @param value
     */
    fun loadTransImage(context: Context, url: String, transType: Int, vararg value: Float) {
        getProxyImpl().loadTransImage(context, url, transType, *value)
    }

    companion object {
        @Volatile
        private var proxyHelp: ImageProxyHelp? = null

        fun getInstance(): ImageProxyHelp {
            return proxyHelp ?: synchronized(this) {
                proxyHelp ?: ImageProxyHelp().also { proxyHelp = it }
            }
        }
    }
}
