package com.leo.imageproxy_ext_glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.leo.imageproxy.IImgProxy
import com.leo.imageproxy.IImgProxyBitmapView
import com.leo.imageproxy.IImgProxyDrawableView
import com.leo.imageproxy.enume.ImgMode
import jp.wasabeef.glide.transformations.CropCircleTransformation
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.MaskTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File

/**
 * Created by LEO
 * On 2019/6/7
 * Description:图片加载库的glide实现
 */
class GlideProxy : IImgProxy {
    /**
     * 加载磁盘中缓存图片文件
     */
    override fun loadDiskFile(context: Context, url: String): File {
        return Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
    }

    override fun loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String, isBitmap: Boolean,
                           imageView: ImageView) {
        var bitmapBuilder: RequestBuilder<Bitmap>? = null
        var gifBuilder: RequestBuilder<GifDrawable>? = null
        if (isBitmap) {
            bitmapBuilder = Glide.with(context)
                    .asBitmap()
                    .load(url)
        } else {
            gifBuilder = Glide.with(context)
                    .asGif()
                    .load(url)
        }
        when (mode) {
            ImgMode.NORMAL -> if (isBitmap) {
                bitmapBuilder!!
                        .apply(RequestOptions.placeholderOf(drawId))
                        .into(imageView)
            } else {
                gifBuilder!!
                        .apply(RequestOptions.placeholderOf(drawId))
                        .into(imageView)
            }
            ImgMode.CIRCULAR -> if (isBitmap) {
                bitmapBuilder!!
                        .apply(RequestOptions.placeholderOf(drawId))
                        .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                        .into(imageView)
            } else {
                gifBuilder!!
                        .apply(RequestOptions.placeholderOf(drawId))
                        .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                        .into(imageView)
            }
            ImgMode.SQUARE -> if (isBitmap) {
                bitmapBuilder!!
                        .apply(RequestOptions.placeholderOf(drawId))
                        .apply(RequestOptions.bitmapTransform(CropSquareTransformation()))
                        .into(imageView)
            } else {
                gifBuilder!!
                        .apply(RequestOptions.placeholderOf(drawId))
                        .apply(RequestOptions.bitmapTransform(CropSquareTransformation()))
                        .into(imageView)
            }
            ImgMode.MASK, ImgMode.NINE_PATCH_MASK -> if (isBitmap) {
                bitmapBuilder!!
                        .apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(),
                                MaskTransformation(drawId))))
                        .into(imageView)
            } else {
                gifBuilder!!
                        .apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(),
                                MaskTransformation(drawId))))
                        .into(imageView)
            }
            else -> {
            }
        }
    }

    override fun <T : IImgProxyBitmapView> loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                                                     bitmapView: T) {
        val bitmapBuilder = Glide.with(context)
                .asBitmap()
                .load(url)
        when (mode) {
            ImgMode.NORMAL -> bitmapBuilder
                    .apply(RequestOptions.placeholderOf(drawId))
                    .into(object : SimpleTarget<Bitmap>(bitmapView.width, bitmapView.height) {
                        override fun onResourceReady(resource: Bitmap,
                                                     transition: Transition<in Bitmap>?) {
                            bitmapView.setBitmap(resource)
                        }
                    })
            ImgMode.CIRCULAR -> bitmapBuilder
                    .apply(RequestOptions.placeholderOf(drawId))
                    .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                    .into(object : SimpleTarget<Bitmap>(bitmapView.width, bitmapView.height) {
                        override fun onResourceReady(resource: Bitmap,
                                                     transition: Transition<in Bitmap>?) {
                            bitmapView.setBitmap(resource)
                        }
                    })
            ImgMode.SQUARE -> bitmapBuilder
                    .apply(RequestOptions.placeholderOf(drawId))
                    .apply(RequestOptions.bitmapTransform(CropSquareTransformation()))
                    .into(object : SimpleTarget<Bitmap>(bitmapView.width, bitmapView.height) {
                        override fun onResourceReady(resource: Bitmap,
                                                     transition: Transition<in Bitmap>?) {
                            bitmapView.setBitmap(resource)
                        }
                    })
            ImgMode.MASK, ImgMode.NINE_PATCH_MASK -> bitmapBuilder
                    .apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(),
                            MaskTransformation(drawId))))
                    .into(object : SimpleTarget<Bitmap>(bitmapView.width, bitmapView.height) {
                        override fun onResourceReady(resource: Bitmap,
                                                     transition: Transition<in Bitmap>?) {
                            bitmapView.setBitmap(resource)
                        }
                    })
            else -> {
            }
        }
    }

    override fun <T : IImgProxyDrawableView> loadImage(context: Context, mode: Int, @DrawableRes drawId: Int, url: String,
                                                       drawableView: T) {
        val gifBuilder = Glide.with(context)
                .asDrawable()
                .load(url)
        when (mode) {
            ImgMode.NORMAL -> gifBuilder
                    .apply(RequestOptions.placeholderOf(drawId))
                    .into(object : SimpleTarget<Drawable>(drawableView.width, drawableView.height) {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            drawableView.setDrawable(resource)
                        }
                    })
            ImgMode.CIRCULAR -> gifBuilder
                    .apply(RequestOptions.placeholderOf(drawId))
                    .apply(RequestOptions.circleCropTransform())
                    .into(object : SimpleTarget<Drawable>(drawableView.width, drawableView.height) {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            drawableView.setDrawable(resource)
                        }
                    })
            ImgMode.SQUARE -> gifBuilder
                    .apply(RequestOptions.placeholderOf(drawId))
                    .apply(RequestOptions.bitmapTransform(CropSquareTransformation()))
                    .into(object : SimpleTarget<Drawable>(drawableView.width, drawableView.height) {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            drawableView.setDrawable(resource)
                        }
                    })
            ImgMode.MASK, ImgMode.NINE_PATCH_MASK -> gifBuilder
                    .apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(),
                            MaskTransformation(drawId))))
                    .into(object : SimpleTarget<Drawable>(drawableView.width, drawableView.height) {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            drawableView.setDrawable(resource)
                        }
                    })
            else -> {
            }
        }
    }

    override fun loadCircularBeadImage(context: Context, url: String, isBitmap: Boolean, px: Int,
                                       imageView: ImageView) {
        if (isBitmap) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(px, 0,
                            RoundedCornersTransformation.CornerType.ALL)))
                    .into(imageView)
        } else {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(px, 0,
                            RoundedCornersTransformation.CornerType.ALL)))
                    .into(imageView)
        }
    }

    override fun <T : IImgProxyBitmapView> loadCircularBeadImage(context: Context, url: String, px: Int,
                                                                 bitmapView: T) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(px, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(object : SimpleTarget<Bitmap>(bitmapView.width, bitmapView.height) {
                    override fun onResourceReady(resource: Bitmap,
                                                 transition: Transition<in Bitmap>?) {
                        bitmapView.setBitmap(resource)
                    }
                })
    }

    override fun <T : IImgProxyDrawableView> loadCircularBeadImage(context: Context, url: String, px: Int,
                                                                   drawableView: T) {
        Glide.with(context)
                .asGif()
                .load(url)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(px, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(object : SimpleTarget<GifDrawable>(drawableView.width, drawableView.height) {
                    override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                        drawableView.setDrawable(resource)
                    }
                })
    }

    /**
     * 提供变换的加载方式
     *
     * @param context
     * @param url
     * @param transType link ImgTransType
     * @param value
     */
    override fun loadTransImage(context: Context, url: String, transType: Int, imageView: ImageView, vararg value: Float) {
    }

    override fun <T : IImgProxyBitmapView> loadTransImage(context: Context, url: String, transType: Int, bitmapView: T, vararg value: Float) {
    }

    override fun <T : IImgProxyDrawableView> loadTransImage(context: Context, url: String, transType: Int, drawableView: T, vararg value: Float) {
    }
}
