package com.leo.commonutil.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by LEO
 * on 2017/5/19.
 */
public class SpUtil {
    private static final String KEY = "com.leo.sp_key";
    private static SpUtil preferencesUtil;

    private SpUtil() {
    }

    public static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static SpUtil getUtil() {
        if (null == preferencesUtil) {
            synchronized (SpUtil.class) {
                if (null == preferencesUtil) {
                    preferencesUtil = new SpUtil();
                }
            }
        }
        return preferencesUtil;
    }

    public boolean put(Context context, @NonNull String key, @NonNull Object o) {
        SharedPreferences sp = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        if (o instanceof String) {
            String s = (String) o;
            return sp.edit().putString(key, s).commit();
        } else if (o instanceof Boolean) {
            boolean b = (boolean) o;
            return sp.edit().putBoolean(key, b).commit();
        } else if (o instanceof Float) {
            float f = (float) o;
            return sp.edit().putFloat(key, f).commit();
        } else if (o instanceof Integer) {
            int i = (int) o;
            return sp.edit().putInt(key, i).commit();
        } else if (o instanceof Long) {
            long l = (long) o;
            return sp.edit().putLong(key, l).commit();
        }
        return false;
    }
}
