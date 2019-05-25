package com.leo.system;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 获取清单文件中meta值工具类
 */
public final class MetaHelp {
    /**
     * 值不可以为纯数字，要不然会报错
     *
     * @param metaKey
     * @return
     */
    public static String getMetaValue(String metaKey) {
        String metaValue = null;
        if (metaKey == null) {
            return null;
        }
        Context context = ContextHelp.getContext();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            metaValue = appInfo.metaData.getString(metaKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return metaValue;
    }
}
