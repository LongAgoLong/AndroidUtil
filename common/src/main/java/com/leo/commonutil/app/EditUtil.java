package com.leo.commonutil.app;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUtil {
    /**
     * 禁止EditText输入空格和换行符
     *
     * @param editText EditText输入框
     */
    public static void setNotInputSpace(EditText editText) {
        editText.setFilters(new InputFilter[]{getSpaceFilter()});
    }

    /**
     * 空格换行输入过滤器
     *
     * @return
     */
    public static InputFilter getSpaceFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText EditText输入框
     */
    public static void setNotInputSpecialChar(EditText editText) {
        editText.setFilters(new InputFilter[]{getSpecialCharFilter()});
    }

    /**
     * 特殊字符输入过滤器
     *
     * @return
     */
    public static InputFilter getSpecialCharFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                String speChat = "[\\p{P}\\s]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
    }
}
