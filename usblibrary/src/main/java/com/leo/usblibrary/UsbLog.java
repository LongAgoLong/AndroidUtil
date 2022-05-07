package com.leo.usblibrary;

import android.util.Log;

import com.github.mjdev.libaums.BuildConfig;

public final class UsbLog {
    private static final String TAG = "USB_";

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG + tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, msg);
        }
    }
}
