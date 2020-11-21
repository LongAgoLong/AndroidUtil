package com.leo.system.util

import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.view.WindowManager
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import com.leo.system.context.ContextHelper

/**
 * 亮度调节工具类
 */
object BrightnessHelp {

    @JvmOverloads
    fun isBrightnessAutoMode(context: Context = ContextHelper.context): Boolean {
        // 获取系统亮度模式
        val brightnessMode = Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)
        return brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
    }

    @JvmOverloads
    fun toggleBrightnessMode(context: Context = ContextHelper.context, @IntRange(from = 0, to = 1) systemMode: Int) {
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, systemMode);
    }

    /**
     * android系统只支持调节/获取手动模式下的亮度值
     */
    @JvmOverloads
    fun adjustSystemBrightness(context: Context = ContextHelper.context,
                               @IntRange(from = 1, to = 255) systemBrightness: Int) {
        if (isBrightnessAutoMode(context)) {
            toggleBrightnessMode(context, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
        }
        // 设置系统亮度
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, systemBrightness)
    }

    /**
     * 调节当前Activity的亮度值
     * @param brightness -1代表跟随系统亮度;0~255代表要调整的亮度
     */
    fun adjustAppBrightness(@NonNull activity: Activity, @IntRange(from = -1, to = 255) brightness: Int) {
        val window = activity.window
        val attributes = window.attributes
        if (brightness == -1) {
            attributes.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        } else {
            attributes.screenBrightness = (if (brightness <= 0) 1 else brightness) / 255f
        }
        window.attributes = attributes
    }
}