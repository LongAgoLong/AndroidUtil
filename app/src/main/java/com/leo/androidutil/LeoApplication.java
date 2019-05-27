package com.leo.androidutil;

import android.app.Application;

import com.leo.androidutil.bitmapproxy.BitmapProxy;
import com.leo.imageproxy.ImageProxyHelp;
import com.leo.system.ContextHelp;
import com.leo.system.LogUtil;
import com.leo.system.enume.LogType;

public class LeoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 优先调用ContextHelp.setContext
         */
        ContextHelp.setContext(this);
        LogUtil.setType(LogType.DEBUG);
        ImageProxyHelp.getInstance().setProxyImpl(new BitmapProxy());
    }
}
