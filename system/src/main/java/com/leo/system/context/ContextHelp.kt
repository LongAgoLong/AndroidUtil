package com.leo.system.context

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

        set(context) {
            weakReference = WeakReference(context.applicationContext)
        }
}
