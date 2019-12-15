package com.leo.commonutil.notify

import android.content.Context
import androidx.annotation.StringRes
import android.view.Gravity
import android.widget.Toast

/**
 * Toast工具类
 */
object ToastUtil {

    private var toast: Toast? = null

    fun initToast(initToast: Toast) {
        toast = initToast
    }

    fun show(context: Context, text: String) {
        if (toast == null) {
            toast = Toast.makeText(context.applicationContext, text, Toast.LENGTH_LONG)
        } else {
            toast!!.duration = Toast.LENGTH_LONG
            toast!!.setText(text)
        }
        toast!!.show()
    }

    fun show(context: Context, @StringRes resId: Int) {
        if (toast == null) {
            toast = Toast.makeText(context.applicationContext, resId, Toast.LENGTH_LONG)
        } else {
            toast!!.duration = Toast.LENGTH_LONG
            toast!!.setText(resId)
        }
        toast!!.show()
    }

    /*
     * duration - Toast.LENGTH_LONG or Toast.LENGTH_SHORT
     * */
    fun show(context: Context, text: String, duration: Int) {
        if (toast == null) {
            toast = Toast.makeText(context.applicationContext, text, duration)
        } else {
            toast!!.duration = duration
            toast!!.setText(text)
        }
        toast!!.show()
    }

    fun show(context: Context, @StringRes resId: Int, duration: Int) {
        if (toast == null) {
            toast = Toast.makeText(context.applicationContext, resId, duration)
        } else {
            toast!!.duration = duration
            toast!!.setText(resId)
        }
        toast!!.show()
    }

    fun cancel() {
        if (null != toast) {
            toast!!.cancel()
        }
    }

    fun showBottom(context: Context, text: String) {
        val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.setText(text)
        toast.show()
    }
}
