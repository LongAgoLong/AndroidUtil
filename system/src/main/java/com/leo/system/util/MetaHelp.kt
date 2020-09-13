package com.leo.system.util

import android.content.pm.PackageManager
import com.leo.system.context.ContextHelp

/**
 * 获取清单文件中meta值工具类
 */
object MetaHelp {
    /**
     * 值不可以为纯数字，要不然会报错
     *
     * @param metaKey
     * @return
     */
    fun getMetaValue(metaKey: String): String? {
        var metaValue: String? = null
        try {
            val appInfo = ContextHelp.context.packageManager.getApplicationInfo(
                    ContextHelp.context.packageName, PackageManager.GET_META_DATA)
            metaValue = appInfo.metaData.getString(metaKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return metaValue
    }
}
