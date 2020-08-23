package com.leo.commonutil.calendar;

/**
 * 预置的日期格式
 */
public class DatePresetFormat {
    private DatePresetFormat() {
    }

    // 带“-”不带结尾
    // 什么都不带以2结尾
    // 带“.”以3结尾
    // 带“/”以4结尾
    // 带中文文本以5结尾
    public static final String DATA_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_YMDHMS2 = "yyyyMMddHHmmss";
    public static final String DATA_YMDHMS3 = "yyyy.MM.dd HH:mm:ss";
    public static final String DATA_YMDHMS4 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATA_YMDHMS5 = "yyyy年MM月dd日 HH:mm:ss";

    public static final String DATA_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATA_YMDHM2 = "yyyyMMddHHmm";
    public static final String DATA_YMDHM3 = "yyyy.MM.dd HH:mm";
    public static final String DATA_YMDHM4 = "yyyy/MM/dd HH:mm";
    public static final String DATA_YMDHM5 = "yyyy年MM月dd日 HH:mm";

    public static final String DATA_YMD = "yyyy-MM-dd";
    public static final String DATA_YMD2 = "yyyyMMdd";
    public static final String DATA_YMD3 = "yyyy.MM.dd";
    public static final String DATA_YMD4 = "yyyy/MM/dd";
    public static final String DATA_YMD5 = "yyyy年MM月dd日";

    public static final String DATA_MDHM = "MM-dd HH:mm";
    public static final String DATA_MDHM2 = "MMddHHmm";
    public static final String DATA_MDHM3 = "yyyy.MM.dd";
    public static final String DATA_MDHM4 = "MM/dd HH:mm";
    public static final String DATA_MDHM5 = "MM月dd日 HH:mm";

    public static final String DATA_MD = "MM-dd";
    public static final String DATA_MD2 = "MMdd";
    public static final String DATA_MD3 = "MM.dd";
    public static final String DATA_MD4 = "MM/dd";
    public static final String DATA_MD5 = "MM月dd日";

    public static final String DATA_HM = "HH:mm";
    public static final String DATA_HMS = "HH:mm:ss";
}
