package com.leo.commonutil.app;

import android.util.Log;

import com.leo.commonutil.enumerate.LogType;

/**
 * 日志公共类
 */
public class LogUtil {
    private static final String TAG_ = LogUtil.class.getSimpleName();
    private static int type = LogType.DEBUG;

    public static void setType(@LogType int type) {
        LogUtil.type = type;
    }

    public static void i(String tag, String msg) {
        if (type == LogType.DEBUG) {
            // 控制台输出
            if (null != msg) {
                Log.i(TAG_ + tag, msg);
            } else {
                Log.i(TAG_ + tag, "the message is null");
            }
        }
    }

    public static void d(String tag, String msg) {
        if (type == LogType.DEBUG) {
            // 控制台输出
            if (null != msg) {
                Log.d(TAG_ + tag, msg);
            } else {
                Log.d(TAG_ + tag, "the message is null");
            }
        }
    }

    public static void e(String tag, String msg) {
        if (type == LogType.DEBUG) {
            // 控制台输出
            if (null != msg) {
                Log.e(TAG_ + tag, msg);
            } else {
                Log.e(TAG_ + tag, "the message is null");
            }
        }
    }

    public static void v(String tag, String msg) {
        if (type == LogType.DEBUG) {
            // 控制台输出
            if (null != msg) {
                Log.v(TAG_ + tag, msg);
            } else {
                Log.v(TAG_ + tag, "the message is null");
            }
        }
    }

    public static void w(String tag, String msg) {
        if (type == LogType.DEBUG) {
            // 控制台输出
            if (null != msg) {
                Log.w(TAG_ + tag, msg);
            } else {
                Log.w(TAG_ + tag, "the message is null");
            }
        }
    }
}
