package com.leo.androidutil

import android.app.Application
import com.leo.imageproxy.ImageProxyHelp
import com.leo.imageproxy_ext_glide.GlideProxy
import com.leo.system.LogUtil
import com.leo.system.enume.LogType

class LeoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtil.setType(LogType.DEBUG)
        ImageProxyHelp.getInstance().setProxyImpl(GlideProxy())
    }
}
