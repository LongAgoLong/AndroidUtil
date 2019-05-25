package com.leo.system;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.leo.system.enume.LogType;

/**
 * 日志公共类
 */
public final class LogUtil {
    private static final String TAG_ = getAppName();
    private static int type = LogType.DEBUG;

    private LogUtil() {
    }

    /**
     * 获取app名称
     *
     * @return
     */
    private static String getAppName() {
        Context context = ContextHelp.getContext();
        PackageManager pm = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(pm).toString();
    }

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
        if (type == LogType.DEBUG || type == LogType.ABTEST) {
            // 控制台输出
            if (null != msg) {
                Log.d(TAG_ + tag, msg);
            } else {
                Log.d(TAG_ + tag, "the message is null");
            }
        }
    }

    public static void e(String tag, String msg) {
        if (type == LogType.DEBUG || type == LogType.ABTEST) {
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
