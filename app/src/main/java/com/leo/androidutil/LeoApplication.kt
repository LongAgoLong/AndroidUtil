package com.leo.androidutil

import android.app.Application
import com.leo.imageproxy.ImgProxy
import com.leo.imageproxy_ext_glide.GlideProxy
import com.leo.system.log.XLog
import com.leo.system.log.LogType

class LeoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        XLog.setType(LogType.DEBUG)
        ImgProxy.getInstance().setProxyImpl(GlideProxy())
    }
}
