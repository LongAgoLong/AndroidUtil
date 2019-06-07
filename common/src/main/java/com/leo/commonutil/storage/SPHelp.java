package com.leo.commonutil.storage;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

/**
 * Created by LEO
 * on 2017/5/19.
 */
public class SPHelp {
    private static final String KEY = "com.leo.sp_key";
    private static SPHelp preferencesUtil;

    private SPHelp() {
    }

    private SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static SPHelp getInstance() {
        if (null == preferencesUtil) {
            synchronized (SPHelp.class) {
                if (null == preferencesUtil) {
                    preferencesUtil = new SPHelp();
                }
            }
        }
        return preferencesUtil;
    }

    public void put(Context context, @NonNull String key, @NonNull Object o) {
        SharedPreferences sp = getSp(context);
        if (o instanceof String) {
            String s = (String) o;
            sp.edit().putString(key, s).apply();
        } else if (o instanceof Boolean) {
            boolean b = (boolean) o;
            sp.edit().putBoolean(key, b).apply();
        } else if (o instanceof Float) {
            float f = (float) o;
            sp.edit().putFloat(key, f).apply();
        } else if (o instanceof Integer) {
            int i = (int) o;
            sp.edit().putInt(key, i).apply();
        } else if (o instanceof Long) {
            long l = (long) o;
            sp.edit().putLong(key, l).apply();
        }
    }

    public boolean putSync(Context context, @NonNull String key, @NonNull Object o) {
        SharedPreferences sp = getSp(context);
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

    public String getString(Context context, @NonNull String key) {
        return getString(context, key, "");
    }

    public String getString(Context context, @NonNull String key, @NonNull String defaultValue) {
        SharedPreferences sp = getSp(context);
        return sp.getString(key, defaultValue);
    }

    public boolean getBoolean(Context context, @NonNull String key) {
        return getBoolean(context, key, false);
    }

    public boolean getBoolean(Context context, @NonNull String key, boolean defaultValue) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, defaultValue);
    }

    public float getFloat(Context context, @NonNull String key) {
        return getFloat(context, key, 0);
    }

    public float getFloat(Context context, @NonNull String key, float defaultValue) {
        SharedPreferences sp = getSp(context);
        return sp.getFloat(key, defaultValue);
    }

    public int getInt(Context context, @NonNull String key) {
        return getInt(context, key, 0);
    }

    public int getInt(Context context, @NonNull String key, int defaultValue) {
        SharedPreferences sp = getSp(context);
        return sp.getInt(key, defaultValue);
    }

    public long getLong(Context context, @NonNull String key) {
        return getLong(context, key, 0L);
    }

    public long getLong(Context context, @NonNull String key, long defaultValue) {
        SharedPreferences sp = getSp(context);
        return sp.getLong(key, defaultValue);
    }
}
