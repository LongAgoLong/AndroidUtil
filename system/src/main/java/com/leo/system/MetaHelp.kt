package com.leo.system

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
    fun getMetaValue(metaKey: String?): String? {
        var metaValue: String? = null
        metaKey ?: return null
        val context = ContextHelp.context
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                    context.packageName, PackageManager.GET_META_DATA)
            metaValue = appInfo.metaData.getString(metaKey)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return metaValue
    }
}
