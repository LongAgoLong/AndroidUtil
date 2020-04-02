package com.leo.system

/**
 * Copyright 2014 Zhenguo Jin (jinzhenguo1990@gmail.com)
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.Surface
import android.view.View
import android.view.Window
import android.view.WindowManager


/**
 * 窗口工具箱
 *
 * @author zhenguo
 */
object WindowUtils {

    /**
     * 获取当前窗口的旋转角度
     *
     * @param activity activity
     * @return int
     */
    fun getDisplayRotation(activity: Activity): Int {
        return when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
    }

    /**
     * 当前是否是横屏
     *
     * @param context context
     * @return boolean
     */
    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 当前是否是竖屏
     *
     * @param context context
     * @return boolean
     */
    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 调整窗口的透明度  1.0f,0.5f 变暗
     *
     * @param from    from>=0&&from<=1.0f
     * @param to      to>=0&&to<=1.0f
     * @param activity 当前的activity
     */
    fun dimBackground(from: Float, to: Float, activity: Activity) {
        val window = activity.window ?: return
        val valueAnimator = ValueAnimator.ofFloat(from, to)
        valueAnimator.duration = 500
        valueAnimator.addUpdateListener { animation ->
            val params = window.attributes
            params.alpha = animation.animatedValue as Float
            window.attributes = params
        }
        valueAnimator.start()
    }

    /**
     * 透明状态栏
     *
     * @param context
     */
    fun setStatusBarTranslucent(context: Context) {
        if (context !is Activity) {
            return
        }
        val window = context.window ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun getKeyboardHeight(paramActivity: Activity): Int {
        var height = (getScreenHeight(paramActivity)
                - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity))
        val preferences = paramActivity
                .getSharedPreferences("com.leo.sp_key", Context.MODE_PRIVATE)
        if (height == 0) {
            //295dp-787为默认软键盘高度 基本差不离
            height = preferences.getInt("KeyboardHeight", dp2px(paramActivity, 295f))
        } else {
            preferences.edit().putInt("KeyboardHeight", height).commit()
        }
        return height
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    fun hideBottomUIMenu(activity: Activity) {
        val decorView = activity.window.decorView
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { // lower api
            decorView.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            // for new api versions.
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 得到设备屏幕的高度
     */
    fun getScreenHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }

    /**
     * 得到设备屏幕的宽度
     */
    fun getScreenWidth(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * statusBar高度
     */
    fun getStatusBarHeight(paramActivity: Activity): Int {
        val localRect = Rect()
        paramActivity.window.decorView.getWindowVisibleDisplayFrame(localRect)
        return localRect.top

    }

    /**
     * 可见屏幕高度
     */
    fun getAppHeight(paramActivity: Activity): Int {
        val localRect = Rect()
        paramActivity.window.decorView.getWindowVisibleDisplayFrame(localRect)
        return localRect.height()
    }

    /**
     * 获取可见内容高度
     * below actionbar, above softkeyboard
     *
     * @param paramActivity
     * @return
     */
    fun getAppContentHeight(paramActivity: Activity): Int {
        return (getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getActionBarHeight(paramActivity) - getKeyboardHeight(paramActivity))
    }

    /**
     * 获取actiobar高度
     */
    fun getActionBarHeight(paramActivity: Activity): Int {
        val attrs = intArrayOf(android.R.attr.actionBarSize)
        val ta = paramActivity.obtainStyledAttributes(attrs)
        return ta.getDimensionPixelSize(0, dp2px(paramActivity, 48f))
                .also {
                    ta.recycle()
                }
    }

    /**
     * 全屏
     */
    fun fullScreen(window: Window?) {
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 保持屏幕常亮
     */
    fun keepScreenOn(window: Window?) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    @JvmOverloads
    fun dp2px(context: Context = ContextHelp.context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    @JvmOverloads
    fun px2dp(context: Context = ContextHelp.context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}
