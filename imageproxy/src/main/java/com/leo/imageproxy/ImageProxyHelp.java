package com.leo.imageproxy;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import android.widget.ImageView;

import com.leo.imageproxy.enume.ImageMode;
import com.leo.imageproxy.enume.ImageTransType;

/**
 * 对外调用方法
 */
public class ImageProxyHelp {
    private static final String TAG = ImageProxyHelp.class.getSimpleName();
    private static ImageProxyHelp proxyHelp;
    private IImageProxy proxyImpl;

    private ImageProxyHelp() {
    }

    public static ImageProxyHelp getInstance() {
        if (null == proxyHelp) {
            synchronized (ImageProxyHelp.class) {
                if (null == proxyHelp) {
                    proxyHelp = new ImageProxyHelp();
                }
            }
        }
        return proxyHelp;
    }

    /**
     * 设置代理实现
     * 只第一次设置有效
     *
     * @param proxyImpl
     */
    public synchronized void setProxyImpl(IImageProxy proxyImpl) {
        if (null == proxyImpl) {
            this.proxyImpl = proxyImpl;
        }
    }

    public IImageProxy getProxyImpl() {
        if (null == proxyImpl) {
            throw new RuntimeException("ImageProxyHelp : must setProxyImpl in application first");
        }
        return proxyImpl;
    }

    /**
     * 普通默认加载
     *
     * @param context
     * @param url
     * @param isBitmap
     * @param imageView
     */
    public void loadImage(Context context, @NonNull String url, boolean isBitmap, ImageView imageView) {
        getProxyImpl().loadImage(context, ImageMode.NORMAL, R.color.place_holder_color, url, isBitmap, imageView);
    }

    public void loadImage(Context context, @NonNull String url, IImageProxyBitmapView bitmapView) {
        getProxyImpl().loadImage(context, ImageMode.NORMAL, R.color.place_holder_color, url, bitmapView);
    }

    public void loadImage(Context context, @NonNull String url, IImageProxyDrawableView drawableView) {
        getProxyImpl().loadImage(context, ImageMode.NORMAL, R.color.place_holder_color, url, drawableView);
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
    public void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, boolean isBitmap, ImageView imageView) {
        getProxyImpl().loadImage(context, mode, drawId, url, isBitmap, imageView);
    }

    public void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, IImageProxyBitmapView bitmapView) {
        getProxyImpl().loadImage(context, mode, drawId, url, bitmapView);
    }

    public void loadImage(Context context, @ImageMode int mode, @DrawableRes int drawId, @NonNull String url, IImageProxyDrawableView drawableView) {
        getProxyImpl().loadImage(context, mode, drawId, url, drawableView);
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
    public void loadCircularBeadImage(Context context, @NonNull String url, boolean isBitmap, int px, ImageView imageView) {
        getProxyImpl().loadCircularBeadImage(context, url, isBitmap, px, imageView);
    }

    public void loadCircularBeadImage(Context context, @NonNull String url, int px, IImageProxyBitmapView bitmapView) {
        getProxyImpl().loadCircularBeadImage(context, url, px, bitmapView);
    }

    public void loadCircularBeadImage(Context context, @NonNull String url, int px, IImageProxyDrawableView drawableView) {
        getProxyImpl().loadCircularBeadImage(context, url, px, drawableView);
    }

    public void loadTransImage(Context context, @NonNull String url, @ImageTransType int transType, float... value) {
        getProxyImpl().loadTransImage(context, url, transType, value);
    }
}
