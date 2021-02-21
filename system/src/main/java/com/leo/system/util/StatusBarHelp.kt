package com.leo.system.util

import android.app.Activity
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

object StatusBarHelp {
    /**
     * 更改状态栏
     *
     * @param activity
     * @param color
     */
    fun setColorRes(activity: Activity?, @ColorRes color: Int) {
        if (null == activity) {
            return
        }
        setColor(activity, activity.resources.getColor(color))
    }

    fun setColor(activity: Activity?, @ColorInt color: Int) {
        if (null == activity) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            if (null != window) {
                window.statusBarColor = color
            }
        }
    }
}