package com.leo.system.log

import android.util.Log
import com.leo.system.context.ContextHelper

/**
 * 日志公共类
 */
object XLog {
    private val TAG_ = appName
    private var type = LogType.VERBOSE

    /**
     * 获取app名称
     *
     * @return
     */
    private val appName: String
        get() {
            val context = ContextHelper.context
            val pm = context.packageManager
            return context.applicationInfo.loadLabel(pm).toString()
        }

    fun setType(@LogType type: Int) {
        XLog.type = type
    }

    fun v(tag: String, msg: String?) {
        if (type <= LogType.VERBOSE) {
            // 控制台输出
            if (null != msg) {
                Log.v("$TAG_$tag", msg)
            } else {
                Log.v("$TAG_$tag", "the message is null")
            }
        }
    }

    fun d(tag: String, msg: String?) {
        if (type <= LogType.DEBUG) {
            // 控制台输出
            if (null != msg) {
                Log.d("$TAG_$tag", msg)
            } else {
                Log.d("$TAG_$tag", "the message is null")
            }
        }
    }

    fun i(tag: String, msg: String?) {
        if (type <= LogType.INFO) {
            // 控制台输出
            if (null != msg) {
                Log.i("$TAG_$tag", msg)
            } else {
                Log.i("$TAG_$tag", "the message is null")
            }
        }
    }

    fun w(tag: String, msg: String?) {
        if (type <= LogType.WARN) {
            // 控制台输出
            if (null != msg) {
                Log.w("$TAG_$tag", msg)
            } else {
                Log.w("$TAG_$tag", "the message is null")
            }
        }
    }

    fun e(tag: String, msg: String?) {
        if (type <= LogType.ERROR) {
            // 控制台输出
            if (null != msg) {
                Log.e("$TAG_$tag", msg)
            } else {
                Log.e("$TAG_$tag", "the message is null")
            }
        }
    }
}
