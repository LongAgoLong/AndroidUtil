package com.leo.commonutil.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import android.text.format.Time;

import com.leo.commonutil.enume.CalendarAddResult;
import com.leo.commonutil.enume.UnitTime;
import com.leo.commonutil.enume.UnitWeek;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by LEO
 * on 2018/12/24
 * 添加日历事件工具类
 */
public final class CalendarUtil {
    public static final String CALENDAR_URI = "content://com.android.calendar/calendars";
    public static final String CALENDAR_EVENT_URI = "content://com.android.calendar/events";
    public static final String CALENDAR_REMINDER_URI = "content://com.android.calendar/reminders";

    private CalendarUtil() {
    }

    /**
     * 检查手机是否存在日历账户，存在则返回账户id，否则返回-1
     */
    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDAR_URI), null, null, null, null);
        try {
            if (null == userCursor) { //查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            try {
                if (null != userCursor) {
                    userCursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String transformDataTime(@UnitTime int unitTime, long time) {
        switch (unitTime) {
            case UnitTime.HOUR:
                return String.valueOf(time * 60 * 60 * 1000);
            case UnitTime.MINUTE:
                return String.valueOf(time * 60 * 1000);
            case UnitTime.SECOND:
                return String.valueOf(time * 1000);
            case UnitTime.MILLIONSECOND:
                return String.valueOf(time);
        }
        return "";
    }

    /**
     * 添加日历事件
     *
     * @param context
     * @param title     标题
     * @param content   内容
     * @param UnitTime  时间单位
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param hasAlarm  是否需要闹钟提醒
     * @return
     */
    @CalendarAddResult
    public static int addCalendarEvent(Context context, @NonNull String title, @NonNull String content, @UnitTime int UnitTime,
                                       long startTime, long endTime, boolean hasAlarm) {
        try {
            int calId = checkCalendarAccount(context);
            if (calId < 0) {
                return CalendarAddResult.NO_USER_ID;
            }
            ContentValues event = new ContentValues();
            event.put("calendar_id", calId);
            event.put("title", title);
            event.put("description", content);
            event.put("dtstart", transformDataTime(UnitTime, startTime));//开始时间
            event.put("dtend", transformDataTime(UnitTime, endTime));//结束时间
            event.put("eventTimezone", Time.getCurrentTimezone());//这个时区一定要设置 否则应用会挂掉
            if (hasAlarm) {
                event.put("hasAlarm", 1);//是否闹钟提醒
            }
            Uri eventcalendarUri = Uri.parse(CALENDAR_EVENT_URI);
            Uri url = context.getContentResolver().insert(eventcalendarUri, event);//添加一条日历事件
            if (null == url) {
                return CalendarAddResult.NO_EVENT_URL;
            }
            long eventId = Long.parseLong(url.getLastPathSegment());//获取这条日历事件的id
            ContentValues values = new ContentValues();
            values.put("event_id", eventId);
            values.put("method", 1);
            values.put("minutes", 24 * 60);
            context.getContentResolver().insert(Uri.parse(CALENDAR_REMINDER_URI), values);
            return CalendarAddResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return CalendarAddResult.OTHER_ERROR;
        }
    }

    /**
     * 时间戳转化具体日历信息
     *
     * @param type 时间戳单位
     * @param time 时间戳
     * @return
     */
    public static CalendarInfo getCalendarInfo(@UnitTime int type, long time) {
        long millionTime;
        switch (type) {
            case UnitTime.MILLIONSECOND:
                millionTime = time;
                break;
            case UnitTime.SECOND:
                millionTime = time * 1000;
                break;
            case UnitTime.MINUTE:
                millionTime = time * 1000 * 60;
                break;
            case UnitTime.HOUR:
                millionTime = time * 1000 * 3600;
                break;
            default:
                throw new RuntimeException("UnitTime error");
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(millionTime));
        int year = cd.get(Calendar.YEAR);
        int month = cd.get(Calendar.MONTH);
        int day = cd.get(Calendar.DAY_OF_MONTH);
        int w = cd.get(Calendar.DAY_OF_WEEK);
        int week;
        switch (w) {
            case Calendar.SUNDAY:
                week = UnitWeek.SUNDAY;
                break;
            case Calendar.MONDAY:
                week = UnitWeek.MONDAY;
                break;
            case Calendar.TUESDAY:
                week = UnitWeek.TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                week = UnitWeek.WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                week = UnitWeek.THURSDAY;
                break;
            case Calendar.FRIDAY:
                week = UnitWeek.FRIDAY;
                break;
            case Calendar.SATURDAY:
                week = UnitWeek.SATURDAY;
                break;
            default:
                throw new RuntimeException("DAY_OF_WEEK error");
        }
        return new CalendarInfo(year, month, day, week);
    }
}
