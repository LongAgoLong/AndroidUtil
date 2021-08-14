package com.leo.system.util

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment

/**
 * @author LEO
 * activity 之间跳转类
 */
object IntentHelper {

    /**
     * @param context
     * @param cls
     * @param finishSelf
     * @param bundle
     * @param clearTop  是否clearTop
     */
    @JvmOverloads
    fun startActivity(context: Context, cls: Class<*>, finishSelf: Boolean = false, bundle: Bundle? = null,
                      clearTop: Boolean = false, singleTop: Boolean = false, newTask: Boolean = false) {
        val intent = generateIntent(context, cls, bundle, clearTop, singleTop, newTask)
        context.startActivity(intent)
        if (finishSelf) {
            if (context is Activity) {
                context.finish()
            }
        }
        // 模仿大众点评网动画跳转效果
        // ((Activity) context).overridePendingTransition(R.anim.anim_enter,
        // R.anim.anim_exit);
        // ((Activity) context).overridePendingTransition(R.anim.slide_in_right,
        // R.anim.slide_out_right);
    }

    @JvmOverloads
    fun startActivity(fragment: Fragment, cls: Class<*>, finishSelf: Boolean = false, bundle: Bundle? = null,
                      clearTop: Boolean = false, singleTop: Boolean = false, newTask: Boolean = false) {
        fragment.activity ?: return
        startActivity(fragment.activity!!, cls, finishSelf, bundle, clearTop, singleTop, newTask)
    }

    private fun generateIntent(context: Context, cls: Class<*>, bundle: Bundle?,
                               clearTop: Boolean, singleTop: Boolean, newTask: Boolean): Intent {
        val intent = Intent()
        intent.setClass(context, cls)
        if (clearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        if (singleTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        return intent
    }

    @JvmOverloads
    fun startActivityByComponent(context: Context, @NonNull pkgPath: String,
                                 @NonNull clsPath: String, bundle: Bundle? = null) {
        val intent = Intent()
        val cmt = ComponentName(pkgPath, clsPath)
        intent.component = cmt
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * startActivityForResult
     *
     * @param activity
     * @param cls
     * @param bundle
     * @param requestCode
     */
    @JvmOverloads
    fun startActivityForResult(activity: Activity, cls: Class<*>, bundle: Bundle? = null, requestCode: Int) {
        val intentRecharge = Intent()
        if (bundle != null) {
            intentRecharge.putExtras(bundle)
        }
        intentRecharge.setClass(activity, cls)
        activity.startActivityForResult(intentRecharge, requestCode)
    }

    @JvmOverloads
    fun startActivityForResult(fragment: Fragment, cls: Class<*>, bundle: Bundle? = null, requestCode: Int) {
        val intentRecharge = Intent()
        if (bundle != null) {
            intentRecharge.putExtras(bundle)
        }
        intentRecharge.setClass(fragment.activity!!, cls)
        fragment.startActivityForResult(intentRecharge, requestCode)
    }
}
