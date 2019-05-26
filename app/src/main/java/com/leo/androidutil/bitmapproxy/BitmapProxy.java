package com.leo.androidutil.bitmapproxy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.leo.imageproxy.IImageProxy;
import com.leo.imageproxy.IImageProxyBitmapView;
import com.leo.imageproxy.IImageProxyDrawableView;
import com.leo.imageproxy.enume.ImageMode;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class BitmapProxy implements IImageProxy {
    @Override
    public void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, boolean isBitmap, ImageView imageView) {
        RequestBuilder<Bitmap> bitmapBuilder = null;
        RequestBuilder<GifDrawable> gifBuilder = null;
        if (isBitmap) {
            bitmapBuilder = Glide.with(context)
                    .asBitmap()
                    .load(url);
        } else {
            gifBuilder = Glide.with(context)
                    .asGif()
                    .load(url);
        }
        switch (mode) {
            case ImageMode.NORMAL:
                if (isBitmap) {
                    bitmapBuilder
                            .apply(RequestOptions.placeholderOf(drawId))
                            .into(imageView);
                } else {
                    gifBuilder
                            .apply(RequestOptions.placeholderOf(drawId))
                            .into(imageView);
                }
                break;
            case ImageMode.CIRCULAR:
                if (isBitmap) {
                    bitmapBuilder
                            .apply(RequestOptions.placeholderOf(drawId))
                            .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                            .into(imageView);
                } else {
                    gifBuilder
                            .apply(RequestOptions.placeholderOf(drawId))
                            .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                            .into(imageView);
                }
                break;
            case ImageMode.SQUARE:
                if (isBitmap) {
                    bitmapBuilder
                            .apply(RequestOptions.placeholderOf(drawId))
                            .apply(RequestOptions.bitmapTransform(new CropSquareTransformation()))
                            .into(imageView);
                } else {
                    gifBuilder
                            .apply(RequestOptions.placeholderOf(drawId))
                            .apply(RequestOptions.bitmapTransform(new CropSquareTransformation()))
                            .into(imageView);
                }
                break;
            case ImageMode.MASK:
            case ImageMode.NINE_PATCH_MASK:
                if (isBitmap) {
                    bitmapBuilder
                            .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new CenterCrop(),
                                    new MaskTransformation(drawId))))
                            .into(imageView);
                } else {
                    gifBuilder
                            .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new CenterCrop(),
                                    new MaskTransformation(drawId))))
                            .into(imageView);
                }
                break;
            case ImageMode.OTHER:
                break;
            case ImageMode.OTHER2:
                break;
            case ImageMode.OTHER3:
                break;
        }
    }

    @Override
    public void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, IImageProxyBitmapView bitmapView) {
        RequestBuilder<Bitmap> bitmapBuilder = Glide.with(context)
                .asBitmap()
                .load(url);
        switch (mode) {
            case ImageMode.NORMAL:
                bitmapBuilder
                        .apply(RequestOptions.placeholderOf(drawId))
                        .into(new SimpleTarget<Bitmap>(bitmapView.getWidth(), bitmapView.getHeight()) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                bitmapView.setBitmap(resource);
                            }
                        });
                break;
            case ImageMode.CIRCULAR:
                bitmapBuilder
                        .apply(RequestOptions.placeholderOf(drawId))
                        .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                        .into(new SimpleTarget<Bitmap>(bitmapView.getWidth(), bitmapView.getHeight()) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                bitmapView.setBitmap(resource);
                            }
                        });
                break;
            case ImageMode.SQUARE:
                bitmapBuilder
                        .apply(RequestOptions.placeholderOf(drawId))
                        .apply(RequestOptions.bitmapTransform(new CropSquareTransformation()))
                        .into(new SimpleTarget<Bitmap>(bitmapView.getWidth(), bitmapView.getHeight()) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                bitmapView.setBitmap(resource);
                            }
                        });
                break;
            case ImageMode.MASK:
            case ImageMode.NINE_PATCH_MASK:
                bitmapBuilder
                        .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new CenterCrop(),
                                new MaskTransformation(drawId))))
                        .into(new SimpleTarget<Bitmap>(bitmapView.getWidth(), bitmapView.getHeight()) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                bitmapView.setBitmap(resource);
                            }
                        });
                break;
            case ImageMode.OTHER:
                break;
            case ImageMode.OTHER2:
                break;
            case ImageMode.OTHER3:
                break;
        }
    }

    @Override
    public void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, IImageProxyDrawableView drawableView) {
        RequestBuilder<Drawable> gifBuilder = Glide.with(context)
                .asDrawable()
                .load(url);
        switch (mode) {
            case ImageMode.NORMAL:
                gifBuilder
                        .apply(RequestOptions.placeholderOf(drawId))
                        .into(new SimpleTarget<Drawable>(drawableView.getWidth(), drawableView.getHeight()) {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                drawableView.setDrawable(resource);
                            }
                        });
                break;
            case ImageMode.CIRCULAR:
                gifBuilder
                        .apply(RequestOptions.placeholderOf(drawId))
                        .apply(RequestOptions.circleCropTransform())
                        .into(new SimpleTarget<Drawable>(drawableView.getWidth(), drawableView.getHeight()) {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                drawableView.setDrawable(resource);
                            }
                        });
                break;
            case ImageMode.SQUARE:
                gifBuilder
                        .apply(RequestOptions.placeholderOf(drawId))
                        .apply(RequestOptions.bitmapTransform(new CropSquareTransformation()))
                        .into(new SimpleTarget<Drawable>(drawableView.getWidth(), drawableView.getHeight()) {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                drawableView.setDrawable(resource);
                            }
                        });
                break;
            case ImageMode.MASK:
            case ImageMode.NINE_PATCH_MASK:
                gifBuilder
                        .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new CenterCrop(),
                                new MaskTransformation(drawId))))
                        .into(new SimpleTarget<Drawable>(drawableView.getWidth(), drawableView.getHeight()) {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                drawableView.setDrawable(resource);
                            }
                        });
                break;
            case ImageMode.OTHER:
                break;
            case ImageMode.OTHER2:
                break;
            case ImageMode.OTHER3:
                break;
        }
    }

    @Override
    public void loadCircularBeadImage(Context context, @NonNull String url, boolean isBitmap, int px, ImageView imageView) {
        if (isBitmap) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(px, 0,
                            RoundedCornersTransformation.CornerType.ALL)))
                    .into(imageView);
        } else {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(px, 0,
                            RoundedCornersTransformation.CornerType.ALL)))
                    .into(imageView);
        }
    }

    @Override
    public void loadCircularBeadImage(Context context, @NonNull String url, int px, IImageProxyBitmapView bitmapView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(px, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(new SimpleTarget<Bitmap>(bitmapView.getWidth(), bitmapView.getHeight()) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmapView.setBitmap(resource);
                    }
                });
    }

    @Override
    public void loadCircularBeadImage(Context context, @NonNull String url, int px, IImageProxyDrawableView drawableView) {
        Glide.with(context)
                .asGif()
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(px, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(new SimpleTarget<GifDrawable>(drawableView.getWidth(), drawableView.getHeight()) {
                    @Override
                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                        drawableView.setDrawable(resource);
                    }
                });
    }

    @Override
    public void loadTransImage(Context context, @NonNull String url, int transType, float... value) {

    }
}
