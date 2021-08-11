package com.leo.system.statusbar

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresPermission
import com.leo.system.rom.RomTarget
import com.leo.system.rom.RomUtil
import java.lang.reflect.Method


object StatusBarHelper {
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

    /**
     * 设置状态栏深色字体以及图标
     */
    fun setLightMode(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                if (RomUtil.rom() == RomTarget.FLYME) {
                    FlymeStatusbarColorUtils.setStatusBarDarkIcon(activity, true)
                } else if (RomUtil.rom() == RomTarget.MIUI) {
                    miuiSetStatusBarLightMode(activity.window, true)
                } else {
                    setAndroidNativeLightStatusBar(activity, true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setDarkMode(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                if (RomUtil.rom() == RomTarget.FLYME) {
                    FlymeStatusbarColorUtils.setStatusBarDarkIcon(activity, false)
                } else if (RomUtil.rom() == RomTarget.MIUI) {
                    miuiSetStatusBarLightMode(activity.window, false)
                } else {
                    setAndroidNativeLightStatusBar(activity, false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     */
    private fun miuiSetStatusBarLightMode(window: Window?, dark: Boolean) {
        if (null != window) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor: View = activity.window.decorView
            if (dark) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.systemUiVisibility = 0
            }
        }
    }

    /**
     * 打开通知栏
     */
    @RequiresPermission(android.Manifest.permission.EXPAND_STATUS_BAR)
    fun expand(context: Context) {
        try {
            val field = Context::class.java.getField("STATUS_BAR_SERVICE")
            field.isAccessible = true
            val name = field.get(Context::class.java) as String
            val service = context.getSystemService(name)
            val statusBarManager = Class
                .forName("android.app.StatusBarManager")
            val expand: Method = if (Build.VERSION.SDK_INT <= 16) {
                statusBarManager.getMethod("expand")
            } else {
                statusBarManager.getMethod("expandNotificationsPanel")
            }
            expand.isAccessible = true
            expand.invoke(service)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 关闭通知栏
     */
    fun collapse(context: Context) {
        try {
            val field = Context::class.java.getField("STATUS_BAR_SERVICE")
            field.isAccessible = true
            val name = field.get(Context::class.java) as String
            val service = context.getSystemService(name)
            val statusBarManager = Class
                .forName("android.app.StatusBarManager")
            val expand: Method = if (Build.VERSION.SDK_INT <= 16) {
                statusBarManager.getMethod("collapse")
            } else {
                statusBarManager.getMethod("collapsePanels")
            }
            expand.isAccessible = true
            expand.invoke(service)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}