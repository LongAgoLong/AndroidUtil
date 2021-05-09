package com.leo.imageproxy

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.leo.imageproxy.enume.ImgMode
import java.io.File

/**
 * 对外调用方法
 */
class ImgProxy private constructor() {
    private var proxyImpl: IImgProxy? = null

    /**
     * 设置代理实现
     * 只第一次设置有效
     *
     * @param proxyImpl
     */
    fun init(proxyImpl: IImgProxy) {
        this.proxyImpl ?: synchronized(this) {
            this.proxyImpl ?: proxyImpl.also { this.proxyImpl = it }
        }
    }

    private fun getProxyImpl(): IImgProxy {
        proxyImpl ?: throw RuntimeException("ImgProxyHelp : must init() in application first")
        return proxyImpl as IImgProxy
    }

    fun loadDiskFile(context: Context, url: String): File {
        return getProxyImpl().loadDiskFile(context, url)
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
    @JvmOverloads
    fun loadImage(context: Context, mode: Int = ImgMode.NORMAL, @DrawableRes drawId: Int = R.color.place_holder_color,
                  url: String, isBitmap: Boolean = false, imageView: ImageView) {
        getProxyImpl().loadImage(context, mode, drawId, url, isBitmap, imageView)
    }

    @JvmOverloads
    fun <T : IImgProxyBitmapView> loadImage(context: Context, mode: Int = ImgMode.NORMAL,
                                            @DrawableRes drawId: Int = R.color.place_holder_color, url: String,
                                            bitmapView: T) {
        getProxyImpl().loadImage(context, mode, drawId, url, bitmapView)
    }

    @JvmOverloads
    fun <T : IImgProxyDrawableView> loadImage(context: Context, mode: Int = ImgMode.NORMAL,
                                              @DrawableRes drawId: Int = R.color.place_holder_color, url: String,
                                              drawableView: T) {
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
    @JvmOverloads
    fun loadCircularBeadImage(context: Context, url: String, isBitmap: Boolean = false, px: Int,
                              imageView: ImageView) {
        getProxyImpl().loadCircularBeadImage(context, url, isBitmap, px, imageView)
    }

    fun <T : IImgProxyBitmapView> loadCircularBeadImage(context: Context, url: String, px: Int, bitmapView: T) {
        getProxyImpl().loadCircularBeadImage(context, url, px, bitmapView)
    }

    fun <T : IImgProxyDrawableView> loadCircularBeadImage(context: Context, url: String, px: Int, drawableView: T) {
        getProxyImpl().loadCircularBeadImage(context, url, px, drawableView)
    }

    /**
     *
     * @param context
     * @param url
     * @param transType {@link ImgTransType}
     * @param value
     */
    fun loadTransImage(context: Context, url: String, transType: Int, imageView: ImageView, vararg value: Float) {
        getProxyImpl().loadTransImage(context, url, transType, imageView, *value)
    }

    fun <T : IImgProxyBitmapView> loadTransImage(context: Context, url: String, transType: Int,
                                                 bitmapView: T, vararg value: Float) {
        getProxyImpl().loadTransImage(context, url, transType, bitmapView, *value)
    }

    fun <T : IImgProxyDrawableView> loadTransImage(context: Context, url: String, transType: Int,
                                                   drawableView: T, vararg value: Float) {
        getProxyImpl().loadTransImage(context, url, transType, drawableView, *value)
    }

    companion object {
        @Volatile
        private var proxy: ImgProxy? = null

        fun getInstance(): ImgProxy {
            return proxy ?: synchronized(this) {
                proxy ?: ImgProxy().also { proxy = it }
            }
        }
    }
}
