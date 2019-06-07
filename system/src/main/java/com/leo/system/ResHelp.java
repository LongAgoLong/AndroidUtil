package com.leo.system;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.ArrayRes;

import static com.leo.system.ContextHelp.getContext;

/**
 * Created by LEO
 * On 2019/6/6
 * Description:资源获取工具类，方便操作资源
 */
public final class ResHelp {

    private ResHelp() {
        throw new UnsupportedOperationException("can't instantiate");
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中定义的字符信息
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到String.xml中定义的字符信息,带占位符
     */
    public static String getString(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    /**
     * 得到String.xml中定义的字符数组信息
     */
    public static String[] getStringArray(@ArrayRes int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到color.xml中定义的颜色信息
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到Drawable资源
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public static int[] getIntArray(@ArrayRes int arrayRes) {
        return getResources().getIntArray(arrayRes);
    }
}
