package com.leo.commonutil.calendar;

import android.content.Context;

import com.leo.commonutil.R;
import com.leo.commonutil.enume.UnitTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间格式化工具类
 * OverWrite by LEO
 * on 2018/9/21
 * at 16:24
 */
public final class DateUtil {
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

    private DateUtil() {
    }

    public static String format(@UnitTime int type, long longTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        switch (type) {
            case UnitTime.HOUR:
                return sdf.format(new Date(longTime * 60 * 60 * 1000));
            case UnitTime.MINUTE:
                return sdf.format(new Date(longTime * 60 * 1000));
            case UnitTime.SECOND:
                return sdf.format(new Date(longTime * 1000));
            case UnitTime.MILLIONSECOND:
                return sdf.format(new Date(longTime));
            default:
                return "";
        }
    }

    public static String format(@UnitTime int type, String time, String format) {
        long longTime = Long.valueOf(time);
        return format(type, longTime, format);
    }

    public static String format(Object o, String format) {
        if (o instanceof Long) {
            long longTime = (long) o;
            return format(UnitTime.SECOND, longTime, format);
        } else if (o instanceof String) {
            String dataSecond = (String) o;
            return format(UnitTime.SECOND, dataSecond, format);
        } else if (o instanceof Integer) {
            int i = (int) o;
            String dataSecond = String.valueOf(i);
            return format(UnitTime.SECOND, dataSecond, format);
        } else if (o instanceof Double) {
            Double d = (double) o;
            String dataSecond = String.valueOf(d.floatValue());
            return format(UnitTime.SECOND, dataSecond, format);
        }
        return "";
    }

    /**
     * 评论时间格式化
     */
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    public static String formatPostTime(String time) {
        try {
            String preFormat = format(time, DATA_YMDHMS);
            SimpleDateFormat format = new SimpleDateFormat(DATA_YMDHMS, Locale.CHINA);
            Date date = format.parse(preFormat);
            long delta = new Date().getTime() - date.getTime();
            if (delta < ONE_MINUTE) {
                long seconds = toSeconds(delta);
                return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
            } else if (delta < 45L * ONE_MINUTE) {
                long minutes = toMinutes(delta);
                return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
            } else if (delta < 24L * ONE_HOUR) {
                long hours = toHours(delta);
                return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
            } else {
                return preFormat.split("\\s+")[0];
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断两个时间戳是否同一天-毫秒级
     *
     * @param ms1
     * @param ms2
     * @return
     */
    public static boolean isSameDay(long ms1, long ms2) {
        if (ms1 == 0L || ms2 == 0L) {
            return false;
        }
        final long interval = ms1 - ms2;
        return Math.abs(interval) < MILLIS_IN_DAY && toDay(ms1) == toDay(ms2);
    }

    private static final int SECONDS_IN_DAY = 60 * 60 * 24;
    private static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    /**
     * 时间戳间隔格式化成中文时间差
     *
     * @param context
     * @param duration
     * @return
     */
    public static String toTimeFormat(Context context, long duration) {
        // 毫秒
        long ssec = duration % 1000;
        // 秒
        long sec = (duration / 1000) % 60;
        // 分钟
        long min = (duration / 1000 / 60) % 60;
        // 小时
        long hour = (duration / 1000 / 60 / 60) % 24;
        // 天
        long day = duration / 1000 / 60 / 60 / 24;

        if (day > 0) {
            return context.getString(R.string.text_day_hour_min_second,
                    String.format(Locale.CHINA, "%02d", day),
                    String.format(Locale.CHINA, "%02d", hour),
                    String.format(Locale.CHINA, "%02d", min),
                    String.format(Locale.CHINA, "%02d", sec));
        }
        if (hour > 0) {
            return context.getString(R.string.text_hour_min_second,
                    String.format(Locale.CHINA, "%02d", hour),
                    String.format(Locale.CHINA, "%02d", min),
                    String.format(Locale.CHINA, "%02d", sec));
        }
        if (min > 0) {
            return context.getString(R.string.text_min_second,
                    String.format(Locale.CHINA, "%02d", min),
                    String.format(Locale.CHINA, "%02d", sec));
        }
        return context.getString(R.string.text_second,
                String.format(Locale.CHINA, "%02d", sec));
    }

    /**
     * 时间转换为时间戳
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @param format  时间对应格式  例如: yyyy-MM-dd
     */
    public static long toTimeStamp(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date;
        try {
            date = simpleDateFormat.parse(timeStr);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
