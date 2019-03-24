package com.leo.commonutil.span;

import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpanUtil {
    public static final String NUMBER = "[0-9]\\d*";
    public static final String IDENTIFY_CARD = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";

    /**
     * 匹配数字
     *
     * @param source
     * @param color
     * @return
     */
    public static SpannableString matchNumber(String source, int color) {
        SpannableString spannableString = new SpannableString(source);
        Pattern pattern = Pattern.compile(NUMBER);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String s = matcher.group();
            if (s != null) {
                int start = matcher.start();
                int end = start + s.length();
                if (end <= spannableString.length()) {
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
                    spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spannableString;
    }

    public static String getMatchNumber(String source) {
        if (!TextUtils.isEmpty(source)) {
            Pattern pattern = Pattern.compile(NUMBER);
            Matcher matcher = pattern.matcher(source);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return "";
    }

    /*
     * 身份证正则匹配
     * */
    public static boolean isIdentifyCard(String identifyCord) {
        if (TextUtils.isEmpty(identifyCord)) {
            return false;
        }
        Pattern pattern = Pattern.compile(IDENTIFY_CARD);
        Matcher matcher = pattern.matcher(identifyCord);
        return matcher.matches();
    }

    /**
     * 禁止EditText输入空格和换行符
     *
     * @param editText EditText输入框
     */
    public static void setETNotInputSpace(EditText editText) {
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            if (source.equals(" ") || source.toString().contentEquals("\n")) {
                return "";
            } else {
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText EditText输入框
     */
    public static void setETNotInputSpecialChar(EditText editText) {
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern pattern = Pattern.compile(speChat);
            Matcher matcher = pattern.matcher(source.toString());
            if (matcher.find()) {
                return "";
            } else if (source.equals(" ") || source.toString().contentEquals("\n")) {
                return "";
            } else {
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
