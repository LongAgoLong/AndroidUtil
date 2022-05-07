package com.leo.system.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.leo.system.database.callback.IETClearFocusEvent
import java.lang.ref.WeakReference

object KeyboardHelper {
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

    /**
     * 设置外部点击隐藏软键盘,传入根布局.
     *
     * @param context
     * @param view
     */
    @JvmOverloads
    @SuppressLint("ClickableViewAccessibility")
    fun setETCancelOnTouchOutside(
        activity: Activity, view: View,
        iETClearFocusEvent: IETClearFocusEvent? = null
    ) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v: View?, event: MotionEvent? ->
                hideSoftKeyboard(activity)
                iETClearFocusEvent?.onClearFocus()
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setETCancelOnTouchOutside(activity, innerView, iETClearFocusEvent)
            }
        }
    }
}