package com.leo.system.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.graphics.drawable.DrawableCompat

object DrawableHelp {

    /**
     * 着色
     *
     * @param context
     * @param colorId
     * @param drawable
     * @return
     */
    fun tint(context: Context, @ColorRes colorId: Int, drawable: Drawable): Drawable {
        val wrapDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrapDrawable, context.resources.getColor(colorId))
        return wrapDrawable
    }

    /**
     * 恢复原来的颜色
     *
     * @param drawable
     * @return
     */
    fun unTint(drawable: Drawable): Drawable {
        val unwrapDrawable = DrawableCompat.unwrap<Drawable>(drawable)
        DrawableCompat.setTintList(unwrapDrawable, null)
        return unwrapDrawable
    }
}
