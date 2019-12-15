package com.leo.system

import android.content.Context
import java.lang.ref.WeakReference

object ContextHelp {
    private var weakReference: WeakReference<Context>? = null

    var context: Context
        get() {
            if (null == weakReference) {
                throw RuntimeException("must setContext() in application first")
            }
            return weakReference!!.get() ?: throw RuntimeException("must setContext() in application first")
        }
        /**
         * 必须在Application中调用
         */
        set(context) {
            weakReference = WeakReference(context.applicationContext)
        }
}
