package com.leo.commonutil.app;

import com.leo.commonutil.enumerate.UnitTime;

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
 * in MoeLove Company
 */
public class DateUtil {
    public static final String DATA_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_YYYY_MM_DD_HH_MM_SS2 = "yyyyMMddHHmmss";
    public static final String DATA_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATA_YYYY_MM_DD_HH_MM2 = "yyyy年MM月dd日 HH:mm";
    public static final String DATA_YYYY_MM_DD_HH_MM3 = "yyyy/MM/dd HH:mm";
    public static final String DATA_YYYY_MM_DD_HH_MM4 = "yyyyMMddHHmm";
    public static final String DATA_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATA_YYYY_MM_DD2 = "yyyy年MM月dd日";
    public static final String DATA_YYYY_MM_DD3 = "yyyy.MM.dd";
    public static final String DATA_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String DATA_MM_DD_HH_MM2 = "MM月dd日 HH:mm";
    public static final String DATA_MM_DD_HH_MM3 = "MM/dd HH:mm";

    public static final String DATA_MM_DD = "MM-dd";
    public static final String DATA_MM_DD1 = "MM/dd";
    public static final String DATA_MM_DD2 = "MM月dd日";
    public static final String DATA_MM_DD3 = "MM月-dd";
    public static final String DATA_HH_MM = "HH:mm";
    public static final String DATA_HH_MM_SS = "HH:mm:ss";

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
        Long longTime = Long.valueOf(time);
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

    /*
     * 评论时间格式化
     * */
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
            String preFormat = format(time, DATA_YYYY_MM_DD_HH_MM_SS);
            SimpleDateFormat format = new SimpleDateFormat(DATA_YYYY_MM_DD_HH_MM_SS, Locale.CHINA);
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

    /*
     * 判断两个时间戳是否同一天-毫秒级
     * */
    public static boolean isSameDay(long ms1, long ms2) {
        if (ms1 == 0L || ms2 == 0L) {
            return false;
        }
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY && toDay(ms1) == toDay(ms2);
    }

    private static final int SECONDS_IN_DAY = 60 * 60 * 24;
    private static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }
}
