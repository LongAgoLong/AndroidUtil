package com.leo.commonutil.storage

import android.content.Context
import android.content.SharedPreferences
import com.leo.system.context.ContextHelp

/**
 * Created by LEO
 * on 2017/5/19.
 */
class SPHelp private constructor() {

    private fun getSp(context: Context): SharedPreferences {
        return context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
    }

    @JvmOverloads
    fun put(context: Context = ContextHelp.context, key: String, o: Any) {
        val sp = getSp(context)
        when (o) {
            is String -> sp.edit().putString(key, o).apply()
            is Boolean -> sp.edit().putBoolean(key, o).apply()
            is Float -> sp.edit().putFloat(key, o).apply()
            is Int -> sp.edit().putInt(key, o).apply()
            is Long -> sp.edit().putLong(key, o).apply()
            else -> throw RuntimeException("put object type is error")
        }
    }

    @JvmOverloads
    fun putSync(context: Context = ContextHelp.context, key: String, o: Any): Boolean {
        val sp = getSp(context)
        return when (o) {
            is String -> sp.edit().putString(key, o).commit()
            is Boolean -> sp.edit().putBoolean(key, o).commit()
            is Float -> sp.edit().putFloat(key, o).commit()
            is Int -> sp.edit().putInt(key, o).commit()
            is Long -> sp.edit().putLong(key, o).commit()
            else -> throw RuntimeException("putSync object type is error")
        }
    }

    @JvmOverloads
    fun getString(context: Context = ContextHelp.context, key: String, defaultValue: String = ""): String? {
        val sp = getSp(context)
        return sp.getString(key, defaultValue)
    }

    @JvmOverloads
    fun getBoolean(context: Context = ContextHelp.context, key: String, defaultValue: Boolean = false): Boolean {
        val sp = getSp(context)
        return sp.getBoolean(key, defaultValue)
    }

    @JvmOverloads
    fun getFloat(context: Context = ContextHelp.context, key: String, defaultValue: Float = 0f): Float {
        val sp = getSp(context)
        return sp.getFloat(key, defaultValue)
    }

    @JvmOverloads
    fun getInt(context: Context = ContextHelp.context, key: String, defaultValue: Int = 0): Int {
        val sp = getSp(context)
        return sp.getInt(key, defaultValue)
    }

    @JvmOverloads
    fun getLong(context: Context = ContextHelp.context, key: String, defaultValue: Long = 0L): Long {
        val sp = getSp(context)
        return sp.getLong(key, defaultValue)
    }

    companion object {
        private const val KEY = "com.leo.sp_key"
        private var instance: SPHelp? = null

        fun getInstance(): SPHelp {
            return instance ?: synchronized(this) {
                instance ?: SPHelp().also { instance = it }
            }
        }
    }
}
