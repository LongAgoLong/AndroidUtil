package com.leo.androidutil

import android.app.Application

import com.leo.imageproxy.ImageProxyHelp
import com.leo.imageproxy_ext_glide.GlideProxy
import com.leo.system.ContextHelp
import com.leo.system.LogUtil
import com.leo.system.enume.LogType

class LeoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /**
         * 优先调用ContextHelp.setContext
         * 其实可以通过contengprovider解决这一步赋值，觉得没必要
         */
        ContextHelp.context = this
        LogUtil.setType(LogType.DEBUG)
        ImageProxyHelp.getInstance().setProxyImpl(GlideProxy())
    }
}