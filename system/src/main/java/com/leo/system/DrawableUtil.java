package com.leo.system;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableUtil {

    /**
     * 着色
     *
     * @param context
     * @param colorId
     * @param drawable
     * @return
     */
    public static Drawable tint(Context context, @ColorRes int colorId, Drawable drawable) {
        Drawable wrapDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(colorId));
        return wrapDrawable;
    }

    /**
     * 恢复原来的颜色
     *
     * @param drawable
     * @return
     */
    public static Drawable unTint(Drawable drawable) {
        Drawable unwrapDrawable = DrawableCompat.unwrap(drawable);
        DrawableCompat.setTintList(unwrapDrawable, null);
        return unwrapDrawable;
    }
}
