package com.leo.imageproxy

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.leo.imageproxy.enume.ImgMode
import java.io.File

/**
 * 对外调用方法
 */
class ImgProxyHelp private constructor() : IImgProxy {
    private var proxyImpl: IImgProxy? = null

    /**
     * 设置代理实现
     * 只第一次设置有效
     *
     * @param proxyImpl
     */
    fun setProxyImpl(proxyImpl: IImgProxy?) {
        if (null != proxyImpl && proxyImpl is ImgProxyHelp) {
            throw java.lang.RuntimeException("cant set a ImgProxyHelp instance")
        }
        this.proxyImpl ?: synchronized(this) {
            this.proxyImpl ?: proxyImpl.also { this.proxyImpl = it }
        }
    }

    private fun getProxyImpl(): IImgProxy {
        proxyImpl ?: throw RuntimeException("ImgProxyHelp : must setProxyImpl in application first")
        return proxyImpl as IImgProxy
    }

    override fun getDiskFile(context: Context, url: String): File {
        return getProxyImpl().getDiskFile(context, url)
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
        getProxyImpl().loadImage(context, ImgMode.NORMAL, R.color.place_holder_color, url, isBitmap, imageView)
    }

    fun loadImage(context: Context, url: String, bitmapView: IImgProxyBitmapView) {
        getProxyImpl().loadImage(context, ImgMode.NORMAL, R.color.place_holder_color, url, bitmapView)
    }

    fun loadImage(context: Context, url: String, drawableView: IImgProxyDrawableView) {
        getProxyImpl().loadImage(context, ImgMode.NORMAL, R.color.place_holder_color, url, drawableView)
    }

    /**
     * 提供普通/圆形/正方形/Mask/NinePatchMask加载方式
     * 也提供ImageMode.OTHER,ImgMode.OTHER2,ImgMode.OTHER3三种自定义实现
     *
     * @param context
     * @param mode
     * @param drawId
     * @param url
     * @param isBitmap
     * @param imageView
     */
    override fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String, isBitmap: Boolean,
                           imageView: ImageView) {
        getProxyImpl().loadImage(context, mode, drawId, url, isBitmap, imageView)
    }

    override fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                           bitmapView: IImgProxyBitmapView) {
        getProxyImpl().loadImage(context, mode, drawId, url, bitmapView)
    }

    override fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                           drawableView: IImgProxyDrawableView) {
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
    override fun loadCircularBeadImage(context: Context, url: String, isBitmap: Boolean, px: Int,
                                       imageView: ImageView) {
        getProxyImpl().loadCircularBeadImage(context, url, isBitmap, px, imageView)
    }

    override fun loadCircularBeadImage(context: Context, url: String, px: Int, bitmapView: IImgProxyBitmapView) {
        getProxyImpl().loadCircularBeadImage(context, url, px, bitmapView)
    }

    override fun loadCircularBeadImage(context: Context, url: String, px: Int,
                                       drawableView: IImgProxyDrawableView) {
        getProxyImpl().loadCircularBeadImage(context, url, px, drawableView)
    }

    /**
     *
     * @param context
     * @param url
     * @param transType {@link ImgTransType}
     * @param value
     */
    override fun loadTransImage(context: Context, url: String, transType: Int, imageView: ImageView, vararg value: Float) {
        getProxyImpl().loadTransImage(context, url, transType, imageView, *value)
    }

    override fun loadTransImage(context: Context, url: String, transType: Int, bitmapView: IImgProxyBitmapView, vararg value: Float) {
        getProxyImpl().loadTransImage(context, url, transType, bitmapView, *value)
    }

    override fun loadTransImage(context: Context, url: String, transType: Int, drawableView: IImgProxyDrawableView, vararg value: Float) {
        getProxyImpl().loadTransImage(context, url, transType, drawableView, *value)
    }

    companion object {
        @Volatile
        private var proxyHelp: ImgProxyHelp? = null

        fun getInstance(): ImgProxyHelp {
            return proxyHelp ?: synchronized(this) {
                proxyHelp ?: ImgProxyHelp().also { proxyHelp = it }
            }
        }
    }
}
