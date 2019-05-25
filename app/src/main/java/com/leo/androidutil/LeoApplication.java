package com.leo.androidutil;

import android.app.Application;

import com.leo.system.ContextHelp;
import com.leo.system.LogUtil;
import com.leo.system.enume.LogType;

public class LeoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 优先调用AppInfoUtil.setContext
         */
        ContextHelp.setContext(this);
        LogUtil.setType(LogType.DEBUG);
    }
}
