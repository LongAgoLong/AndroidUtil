package com.leo.commonutil.notify

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.leo.system.context.ContextHelp

/**
 * Toast工具类
 */
object ToastUtil {
    private var toast: Toast? = null

    fun initToast(@LayoutRes layoutId: Int) {
        cancel()
        toast = Toast(ContextHelp.context)
        toast!!.view = LayoutInflater.from(ContextHelp.context).inflate(layoutId, null)
    }

    @SuppressLint("ShowToast")
    @JvmOverloads
    fun show(gravity: Int = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,
             xOffset: Int = 0, yOffset: Int = 0, text: String,
             duration: Int = Toast.LENGTH_LONG) {
        if (toast == null) {
            toast = Toast.makeText(ContextHelp.context, text, duration)
            toast!!.setGravity(gravity, xOffset, yOffset)
        } else {
            toast!!.duration = duration
            toast!!.setGravity(gravity, xOffset, yOffset)
            toast!!.setText(text)
        }
        toast!!.show()
    }

    @SuppressLint("ShowToast")
    @JvmOverloads
    fun show(gravity: Int = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,
             xOffset: Int = 0, yOffset: Int = 0, @StringRes resId: Int,
             duration: Int = Toast.LENGTH_LONG) {
        if (toast == null) {
            toast = Toast.makeText(ContextHelp.context, resId, duration)
            toast!!.setGravity(gravity, xOffset, yOffset)
        } else {
            toast!!.duration = duration
            toast!!.setGravity(gravity, xOffset, yOffset)
            toast!!.setText(resId)
        }
        toast!!.show()
    }

    fun cancel() {
        if (null != toast) {
            toast!!.cancel()
        }
    }
}
