package com.leo.androidutil;

import android.app.Application;

import com.leo.commonutil.app.AppInfoUtil;
import com.leo.commonutil.app.LogUtil;
import com.leo.commonutil.enumerate.LogType;

public class LeoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 优先调用AppInfoUtil.setContext
         */
        AppInfoUtil.setContext(this);
        LogUtil.setType(LogType.DEBUG);
    }
}
