package com.leo.androidutil

import android.app.Application
import com.leo.imageproxy.ImgProxyHelp
import com.leo.imageproxy_ext_glide.GlideProxy
import com.leo.system.log.LogUtil
import com.leo.system.log.LogType

class LeoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LogUtil.setType(LogType.DEBUG)
        ImgProxyHelp.getInstance().setProxyImpl(GlideProxy())
    }
}
