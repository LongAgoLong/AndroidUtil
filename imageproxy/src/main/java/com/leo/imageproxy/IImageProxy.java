package com.leo.imageproxy;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import android.widget.ImageView;

import com.leo.imageproxy.enume.ImageMode;
import com.leo.imageproxy.enume.ImageTransType;

public interface IImageProxy {

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
    void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, boolean isBitmap, ImageView imageView);

    void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, IImageProxyBitmapView bitmapView);

    void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, IImageProxyDrawableView drawableView);

    /**
     * 圆角加载方式
     *
     * @param context
     * @param url
     * @param isBitmap
     * @param px
     * @param imageView
     */
    void loadCircularBeadImage(Context context, @NonNull String url, boolean isBitmap, int px, ImageView imageView);

    void loadCircularBeadImage(Context context, @NonNull String url, int px, IImageProxyBitmapView bitmapView);

    void loadCircularBeadImage(Context context, @NonNull String url, int px, IImageProxyDrawableView drawableView);

    /**
     * 提供变换的加载方式
     *
     * @param context
     * @param url
     * @param transType
     * @param value
     */
    void loadTransImage(Context context, @NonNull String url, @ImageTransType int transType, float... value);
}
