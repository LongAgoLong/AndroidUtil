package com.leo.system

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.annotation.ArrayRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by LEO
 * On 2019/6/6
 * Description:资源获取工具类，方便操作资源
 */
class ResHelp private constructor() {
    init {
        throw UnsupportedOperationException("can't instantiate")
    }

    companion object {
        /**
         * 得到Resource对象
         */
        val resources: Resources
            get() = ContextHelp.context.resources

        /**
         * 得到String.xml中定义的字符信息
         */
        fun getString(resId: Int): String {
            return resources.getString(resId)
        }

        /**
         * 得到String.xml中定义的字符信息,带占位符
         */
        fun getString(resId: Int, vararg formatArgs: Any): String {
            return resources.getString(resId, *formatArgs)
        }

        /**
         * 得到String.xml中定义的字符数组信息
         */
        fun getStringArray(@ArrayRes resId: Int): Array<String> {
            return resources.getStringArray(resId)
        }

        /**
         * 得到color.xml中定义的颜色信息
         */
        fun getColor(resId: Int): Int {
            return resources.getColor(resId)
        }

        /**
         * 得到Drawable资源
         */
        fun getDrawable(resId: Int): Drawable {
            return resources.getDrawable(resId)
        }

        fun getIntArray(@ArrayRes arrayRes: Int): IntArray {
            return resources.getIntArray(arrayRes)
        }

        /**
         * get an asset using ACCESS_STREAMING mode. This provides access to files that have been bundled with an
         * application as assets -- that is, files placed in to the "assets" directory.
         *
         * @param fileName The name of the asset to open. This name can be hierarchical.
         * @return geFileFromAssets
         */
        fun geFileFromAssets(fileName: String): String? {
            if (TextUtils.isEmpty(fileName)) {
                return null
            }
            val s = StringBuilder()
            try {
                val input = InputStreamReader(resources.assets.open(fileName))
                val br = BufferedReader(input)
                var line: String
                while (br.readLine().also { line = it } != null) {
                    s.append(line)
                }
                return s.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * get content from a raw resource. This can only be used with resources whose value is the name of an asset files
         * -- that is, it can be used to open drawable, sound, and raw resources; it will fail on string and color
         * resources.
         *
         * @param resId The resource identifier to open, as generated by the appt tool.
         * @return geFileFromRaw
         */
        fun geFileFromRaw(resId: Int): String? {
            val s = StringBuilder()
            try {
                val input = InputStreamReader(resources.openRawResource(resId))
                val br = BufferedReader(input)
                var line: String
                while (br.readLine().also { line = it } != null) {
                    s.append(line)
                }
                return s.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }
    }
}
