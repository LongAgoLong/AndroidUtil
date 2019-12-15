package com.leo.system

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * @author sunny TODO activity 之间跳转类
 */
object IntentUtil {

    /**
     * @param context
     * @param cls
     * @param finishSelf
     * @param bundle
     * @param clear_top  是否clearTop
     */
    @JvmOverloads
    fun startActivity(context: Context, cls: Class<*>, finishSelf: Boolean = false, bundle: Bundle? = null, clear_top: Boolean = false,
                      single_top: Boolean = false) {
        val it = Intent()
        it.setClass(context, cls)
        if (clear_top) {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        if (single_top) {
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        if (bundle != null) {
            it.putExtras(bundle)
        }
        context.startActivity(it)
        if (finishSelf) {
            val activity = context as Activity
            activity.finish()
        }
        // 模仿大众点评网动画跳转效果
        // ((Activity) context).overridePendingTransition(R.anim.anim_enter,
        // R.anim.anim_exit);
        // ((Activity) context).overridePendingTransition(R.anim.slide_in_right,
        // R.anim.slide_out_right);
    }

    /**
     * startActivityForResult
     *
     * @param context
     * @param cls
     * @param bundle
     * @param requestCode
     */
    fun startActivityForResult(context: Context, cls: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intentRecharge = Intent()
        if (bundle != null) {
            intentRecharge.putExtras(bundle)
        }
        intentRecharge.setClass(context, cls)
        (context as Activity).startActivityForResult(intentRecharge, requestCode)
    }
}
