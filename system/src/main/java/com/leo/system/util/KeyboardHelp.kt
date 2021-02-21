package com.leo.system.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.lang.ref.WeakReference

object KeyboardHelp {
    /**
     * 键盘是否在显示
     *
     * @param paramActivity
     * @return
     */
    fun isSoftKeyboardShow(paramActivity: Activity?): Boolean {
        val height = (WindowUtils.getScreenHeight(paramActivity!!)
                - WindowUtils.getStatusBarHeight(paramActivity)
                - WindowUtils.getAppHeight(paramActivity))
        return height != 0
    }

    /**
     * 显示键盘
     *
     * @param mContext
     * @param paramEditText
     */
    fun showSoftKeyboard(mContext: Context, paramEditText: EditText) {
        val weakReference = WeakReference(mContext)
        paramEditText.requestFocus()
        paramEditText.post {
            val context = weakReference.get() ?: return@post
            val imm = context.applicationContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(paramEditText, 0)
        }
    }

    /**
     * 关闭软键盘
     *
     * @param mContext  上下文
     * @param mEditText 获得焦点的输入框
     */
    fun hideSoftKeyboard(mContext: Context, mEditText: EditText) {
        try {
            mEditText.clearFocus()
            val imm = mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 关闭软键盘
     *
     * @param activity
     */
    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager = activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocus = activity.currentFocus
            currentFocus ?: return
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}