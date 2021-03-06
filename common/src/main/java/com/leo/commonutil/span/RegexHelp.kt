/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leo.commonutil.span

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

import java.util.regex.Pattern

/**
 * <h2>正则表达式工具类，提供一些常用的正则表达式</h2>
 */
object RegexHelp {

    val NUMBER = "[0-9]\\d*"
    /**
     * 匹配全网IP的正则表达式
     */
    val IP_REGEX = "^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$"

    /**
     * 匹配手机号码的正则表达式
     * <br></br>支持130——139、150——153、155——159、180、183、185、186、188、189号段
     */
    val PHONE_NUMBER_REGEX = "^1(3\\d|5[012356789]|8[035689])\\d{8}$"

    /**
     * 匹配邮箱的正则表达式
     * <br></br>"www."可省略不写
     */
    val EMAIL_REGEX = "^(www\\.)?\\w+@\\w+(\\.\\w+)+$"

    /**
     * 匹配汉子的正则表达式，个数限制为一个或多个
     */
    val CHINESE_REGEX = "^[\u4e00-\u9f5a]+$"

    /**
     * 匹配正整数的正则表达式，个数限制为一个或多个
     */
    val POSITIVE_INTEGER_REGEX = "^\\d+$"

    /**
     * 匹配身份证号的正则表达式
     */
    val ID_CARD = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$"

    /**
     * 匹配邮编的正则表达式
     */
    val ZIP_CODE = "^\\d{6}$"

    /**
     * 匹配URL的正则表达式
     */
    val URL = "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))://[wW]{3}\\.[\\w-]+\\.\\w{2,4}(/.*)?$"
    val HTML = "(https|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"

    /**
     * 匹配给定的字符串是否是一个邮箱账号，"www."可省略不写
     *
     * @param string 给定的字符串
     * @return true：是
     */
    fun isEmail(string: String): Boolean {
        return string.matches(EMAIL_REGEX.toRegex())
    }

    /**
     * 匹配给定的字符串是否是一个手机号码，支持130——139、150——153、155——159、180、183、185、186、188、189号段
     *
     * @param string 给定的字符串
     * @return true：是
     */
    fun isMobilePhoneNumber(string: String): Boolean {
        return string.matches(PHONE_NUMBER_REGEX.toRegex())
    }

    /**
     * 匹配给定的字符串是否是一个全网IP
     *
     * @param string 给定的字符串
     * @return true：是
     */
    fun isIp(string: String): Boolean {
        return string.matches(IP_REGEX.toRegex())
    }

    /**
     * 匹配给定的字符串是否全部由汉字组成
     *
     * @param string 给定的字符串
     * @return true：是
     */
    fun isChinese(string: String): Boolean {
        return string.matches(CHINESE_REGEX.toRegex())
    }

    /**
     * 验证给定的字符串是否全部由正整数组成
     *
     * @param string 给定的字符串
     * @return true：是
     */
    fun isPositiveInteger(string: String): Boolean {
        return string.matches(POSITIVE_INTEGER_REGEX.toRegex())
    }

    /**
     * 验证给定的字符串是否是身份证号
     * <br></br>
     * <br></br>身份证15位编码规则：dddddd yymmdd xx p
     * <br></br>dddddd：6位地区编码
     * <br></br>yymmdd：出生年(两位年)月日，如：910215
     * <br></br>xx：顺序编码，系统产生，无法确定
     * <br></br>p：性别，奇数为男，偶数为女
     * <br></br>
     * <br></br>
     * <br></br>身份证18位编码规则：dddddd yyyymmdd xxx y
     * <br></br>dddddd：6位地区编码
     * <br></br>yyyymmdd：出生年(四位年)月日，如：19910215
     * <br></br>xxx：顺序编码，系统产生，无法确定，奇数为男，偶数为女
     * <br></br>y：校验码，该位数值可通过前17位计算获得
     * <br></br>前17位号码加权因子为 Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
     * <br></br>验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]
     * <br></br>如果验证码恰好是10，为了保证身份证是十八位，那么第十八位将用X来代替 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )
     * <br></br>i为身份证号码1...17 位; Y_P为校验码Y所在校验码数组位置
     *
     * @param string
     * @return
     */
    fun isIdCard(string: String): Boolean {
        return string.matches(ID_CARD.toRegex())
    }

    /**
     * 验证给定的字符串是否是邮编
     *
     * @param string
     * @return
     */
    fun isPostcode(string: String): Boolean {
        return string.matches(ZIP_CODE.toRegex())
    }

    /**
     * 验证给定的字符串是否是URL，仅支持http、https、ftp
     *
     * @param string
     * @return
     */
    fun isURL(string: String): Boolean {
        return string.matches(URL.toRegex())
    }

    /**
     * 高亮匹配数字
     *
     * @param source
     * @param color
     * @return
     */
    fun highLightNumber(source: String, @ColorInt color: Int): SpannableString {
        val spannableString = SpannableString(source)
        val pattern = Pattern.compile(NUMBER)
        val matcher = pattern.matcher(spannableString)
        while (matcher.find()) {
            val s = matcher.group()
            if (s != null) {
                val start = matcher.start()
                val end = start + s.length
                if (end <= spannableString.length) {
                    val colorSpan = ForegroundColorSpan(color)
                    spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        return spannableString
    }

    fun getMatchNumber(source: String): String {
        if (!TextUtils.isEmpty(source)) {
            val pattern = Pattern.compile(NUMBER)
            val matcher = pattern.matcher(source)
            if (matcher.find()) {
                return matcher.group()
            }
        }
        return ""
    }

    fun highLightAndClickHtml(context: Context, textView: TextView, source: String, replaceStr: String,
                              @ColorRes color: Int, mIXClickCallback: IXClickCallback?): XMovementMethod {
        val spannableString = SpannableStringBuilder(source)
        val movementMethod = XMovementMethod.getInstance(context)
        //设置正则
        val pattern = Pattern.compile(HTML)
        val matcher = pattern.matcher(spannableString)

        // 要实现文字的点击效果，这里需要做特殊处理
        textView.movementMethod = movementMethod
        if (matcher.find()) {
            // 重置正则位置
            matcher.reset()
        }
        while (matcher.find()) {
            val html = matcher.group()
            val start = matcher.start()
            val end = start + html.length
            if (end <= spannableString.length) {
                val sbs = SpannableString(replaceStr)
                spannableString.replace(start, end, sbs)
                val clickableSpan = object : XClickableSpan(color) {
                    override fun onClick(widget: View) {
                        mIXClickCallback?.onClick(widget, html)
                    }
                }
                spannableString.setSpan(clickableSpan, start, start + sbs.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        textView.text = spannableString
        return movementMethod
    }
}
