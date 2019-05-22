package com.leo.commonutil.app;

import android.content.Context;

import java.lang.ref.WeakReference;

public class ContextHelp {
    private static WeakReference<Context> weakReference;

    private ContextHelp() {
    }

    /**
     * 必须在Application中调用
     *
     * @param context
     */
    public static void setContext(Context context) {
        ContextHelp.weakReference = new WeakReference<>(context.getApplicationContext());
    }

    public static Context getContext() {
        if (null == weakReference) {
            throw new RuntimeException("must setContext() in application first");
        }
        Context context = weakReference.get();
        if (null == context) {
            throw new RuntimeException("must setContext() in application first");
        }
        return context;
    }
}
