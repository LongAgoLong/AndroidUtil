package com.leo.pinyinlib

import android.text.TextUtils
import androidx.annotation.NonNull
import com.leo.system.ResHelp
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination

/**
 * 拼音转化工具类
 */
class PinyinHelp private constructor() {
    private val format: HanyuPinyinOutputFormat = HanyuPinyinOutputFormat()
    private val filterMap: HashMap<String, String> = HashMap()

    companion object {
        private const val PINYIN_FILTER = "pinyin_filter.txt"
        private const val PINYIN_CUSTOM_FILTER = "pinyin_cst_filter.txt"
        @Volatile
        private var instance: PinyinHelp? = null

        fun getInstance(): PinyinHelp {
            return instance ?: synchronized(this) {
                instance ?: PinyinHelp().also { instance = it }
            }
        }
    }

    init {
        format.caseType = HanyuPinyinCaseType.LOWERCASE
        format.toneType = HanyuPinyinToneType.WITHOUT_TONE
        format.vCharType = HanyuPinyinVCharType.WITH_V
        initCstParsing()
    }

    @JvmOverloads
    fun initConfig(caseType: HanyuPinyinCaseType = HanyuPinyinCaseType.LOWERCASE,
                   toneType: HanyuPinyinToneType = HanyuPinyinToneType.WITHOUT_TONE,
                   vCharType: HanyuPinyinVCharType = HanyuPinyinVCharType.WITH_V) {
        format.caseType = caseType
        format.toneType = toneType
        format.vCharType = vCharType
    }

    private fun initCstParsing() {
        /**
         * pinyin4j会将‘这’转换为‘zhei’，‘那’转换为‘nei’
         * 因此提供一个HashMap用于注入部分自定义纠正
         */
        val s = ResHelp.getFileFromAssets(PINYIN_FILTER)
        if (!TextUtils.isEmpty(s)) {
            val list = s!!.split("#")
            list.forEach {
                if (TextUtils.isEmpty(it)) {
                    return@forEach
                }
                val split = it.split(",")
                if (split.size == 2) {
                    filterMap[split[0]] = split[1]
                }
            }
        }
        /**
         * 提供一个放置于assets文件夹下的pinyin_cst_filter.txt文件供使用者赋值
         * 格式参考pinyin_filter.txt
         */
        val cst = ResHelp.getFileFromAssets(PINYIN_CUSTOM_FILTER)
        if (!TextUtils.isEmpty(cst)) {
            val list = cst!!.split("#")
            list.forEach {
                if (TextUtils.isEmpty(it)) {
                    return@forEach
                }
                val split = it.split(",")
                if (split.size == 2) {
                    filterMap[split[0]] = split[1]
                }
            }
        }

    }

    /**
     * 名称转化为拼音
     * 转化失败时默认返回#
     */
    fun parse(@NonNull str: String): String {
        val input = str.trim().toCharArray()
        var output = ""
        try {
            for (curChar in input) {
                val s = curChar.toString()
                output += when {
                    filterMap.containsKey(s) -> {
                        filterMap[s]
                    }
                    s.matches("[\\u4E00-\\u9FA5]+".toRegex()) -> {
                        val temp = PinyinHelper.toHanyuPinyinStringArray(curChar, format)
                        temp[0]
                    }
                    else -> s
                }
            }
        } catch (e: BadHanyuPinyinOutputFormatCombination) {
            e.printStackTrace()
            output = "#"
        }
        return output
    }

    /**
     * 获取第一个文字拼音的首字母
     * 转化失败时默认返回#
     */
    fun parseFirstLetter(@NonNull str: String): String {
        val s = str.substring(0, 1)
        val parsePinYin = parse(s)
        return if (parsePinYin.length == 1) {
            parsePinYin
        } else {
            parsePinYin.substring(0, 1)
        }
    }

    /**
     * 获取每个汉字拼音的首字母，返回字符串
     */
    fun parseAllFirstLetter(@NonNull str: String): String {
        val length = str.length
        val strBuilder = StringBuilder()
        for (i in 0 until length) {
            strBuilder.append(parseFirstLetter(str.substring(i, i + 1)))
        }
        return strBuilder.toString()
    }

}