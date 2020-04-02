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
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

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

    /**
     * 获取姓名的全拼
     *
     * @param contactName 姓名
     * @return 返回格式有两种，一种是[li][zhong]xin:[li][chong]xin，有:分割代表有多音字返回多种可能结果
     * 另一种tonywang[xin]，没有:分割
     * []包裹代表是一个汉字转化来的拼音
     */
    fun getNameSimpleQuanpin(name: String): String? {
        // 只保留数字、字母、汉字
        val contactName = name.replace(Regex("[^0-9a-zA-Z\\u4E00-\\u9FA5]+"), "")
        val allQuanPin = ArrayList<ArrayList<String>>()
        val chars = contactName.toCharArray()
        for (ch in chars) {
            val oneCharaPinYin = ArrayList<String>()
            try {
                if (ch.toString().matches(Regex("[\\u4E00-\\u9FA5]+"))) {
                    val results = PinyinHelper.toHanyuPinyinStringArray(ch, format)
                    for (result in results) {
                        val s = "[$result]"
                        oneCharaPinYin.add(s)
                    }
                } else {
                    oneCharaPinYin.add(ch.toString())
                }
            } catch (e1: BadHanyuPinyinOutputFormatCombination) {
                e1.printStackTrace()
            }
            if (oneCharaPinYin.size != 0) {
                allQuanPin.add(oneCharaPinYin)
            }
        }
        val sb = StringBuffer(0)
        val ret = getAllCombine(allQuanPin)
        if (!ret.isNullOrEmpty()) {
            for (s in ret) {
                sb.append(s).append(":")
            }
        } else {
            sb.append(':')
        }
        return sb.substring(0, sb.lastIndexOf(":")).toLowerCase(Locale.getDefault())
    }

    private fun getAllCombine(ll: ArrayList<ArrayList<String>>): List<String>? {
        val ret: MutableList<String> = ArrayList()
        val bk: MutableList<String> = ArrayList()
        var count = 1
        var length: Int
        val temp = StringBuffer(0)
        for (list in ll) {
            length = list.size
            for (j in 0 until count) {
                for (i in 0 until length) {
                    if (0 != ret.size) {
                        temp.append(ret[j])
                    }
                    temp.append(list[i])
                    bk.add(temp.toString())
                    temp.setLength(0)
                }
            }
            count *= length
            ret.clear()
            ret.addAll(bk)
            bk.clear()
        }
        return ret
    }
}