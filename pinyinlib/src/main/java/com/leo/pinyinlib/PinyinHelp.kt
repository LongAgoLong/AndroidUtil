package com.leo.pinyinlib

import androidx.annotation.NonNull
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音转化工具类
 */
class PinyinHelp {
    private val format: HanyuPinyinOutputFormat = HanyuPinyinOutputFormat()

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            PinyinHelp()
        }
    }

    init {
        format.caseType = HanyuPinyinCaseType.LOWERCASE
        format.toneType = HanyuPinyinToneType.WITHOUT_TONE
        format.vCharType = HanyuPinyinVCharType.WITH_V
    }

    /**
     * 名称转化为拼音
     * 转化失败时默认返回#
     */
    fun parsePinyin(@NonNull str: String): String {
        val input = str.trim().toCharArray()
        var output = ""
        try {
            for (curChar in input) {
                output += if (curChar.toString().matches("[\\u4E00-\\u9FA5]+".toRegex())) {
                    val temp = PinyinHelper.toHanyuPinyinStringArray(curChar, format)
                    temp[0]
                } else {
                    curChar.toString()
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
    fun parsePinyinFirstLetter(@NonNull str: String): String {
        val s = str.substring(0, 1)
        val parsePinYin = parsePinyin(s)
        return if (parsePinYin.length == 1) {
            parsePinYin
        } else {
            parsePinYin.substring(0, 1)
        }
    }

    /**
     * 获取每个汉字拼音的首字母，返回字符串
     */
    fun parsePinyinAllFirstLetter(@NonNull str: String): String {
        val length = str.length
        val strBuilder = StringBuilder()
        for (i in 0 until length) {
            strBuilder.append(parsePinyinFirstLetter(str.substring(i, i + 1)))
        }
        return strBuilder.toString()
    }

}