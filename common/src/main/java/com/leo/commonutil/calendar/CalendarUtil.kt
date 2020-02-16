package com.leo.commonutil.calendar

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.text.format.Time
import com.leo.commonutil.enume.CalendarAddResult
import com.leo.commonutil.enume.UnitTime
import com.leo.commonutil.enume.UnitWeek
import com.leo.commonutil.storage.IOUtil
import java.util.*

/**
 * Created by LEO
 * on 2018/12/24
 * 添加日历事件工具类
 */
object CalendarUtil {
    private const val CALENDAR_URI = "content://com.android.calendar/calendars"
    private const val CALENDAR_EVENT_URI = "content://com.android.calendar/events"
    private const val CALENDAR_REMINDER_URI = "content://com.android.calendar/reminders"

    /**
     * 检查手机是否存在日历账户，存在则返回账户id，否则返回-1
     */
    private fun checkCalendarAccount(context: Context): Int {
        val userCursor = context.contentResolver.query(Uri.parse(CALENDAR_URI), null, null, null, null)
        try {
            //查询返回空值
            userCursor ?: return -1
            val count = userCursor.count
            return if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst()
                val result = userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID))
                IOUtil.closeQuietly(userCursor)
                result
            } else {
                -1
            }
        } finally {
            IOUtil.closeQuietly(userCursor!!)
        }
    }

    private fun transformDataTime(@UnitTime unitTime: Int, time: Long): String {
        when (unitTime) {
            UnitTime.HOUR -> return (time * 60 * 60 * 1000).toString()
            UnitTime.MINUTE -> return (time * 60 * 1000).toString()
            UnitTime.SECOND -> return (time * 1000).toString()
            UnitTime.MILLIONSECOND -> return time.toString()
        }
        return ""
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
    fun addCalendarEvent(context: Context, title: String, content: String,
                         @UnitTime UnitTime: Int,
                         startTime: Long, endTime: Long, hasAlarm: Boolean): Int {
        try {
            val calId = checkCalendarAccount(context)
            if (calId < 0) {
                return CalendarAddResult.NO_USER_ID
            }
            val event = ContentValues()
            event.put("calendar_id", calId)
            event.put("title", title)
            event.put("description", content)
            event.put("dtstart", transformDataTime(UnitTime, startTime))//开始时间
            event.put("dtend", transformDataTime(UnitTime, endTime))//结束时间
            event.put("eventTimezone", Time.getCurrentTimezone())//这个时区一定要设置 否则应用会挂掉
            if (hasAlarm) {
                event.put("hasAlarm", 1)//是否闹钟提醒
            }
            val eventcalendarUri = Uri.parse(CALENDAR_EVENT_URI)
            val url = context.contentResolver.insert(eventcalendarUri, event)
                    ?: return CalendarAddResult.NO_EVENT_URL//添加一条日历事件
            val eventId = java.lang.Long.parseLong(url.lastPathSegment!!)//获取这条日历事件的id
            val values = ContentValues()
            values.put("event_id", eventId)
            values.put("method", 1)
            values.put("minutes", 24 * 60)
            context.contentResolver.insert(Uri.parse(CALENDAR_REMINDER_URI), values)
            return CalendarAddResult.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            return CalendarAddResult.OTHER_ERROR
        }

    }

    /**
     * 时间戳转化具体日历信息
     *
     * @param type 时间戳单位
     * @param time 时间戳
     * @return
     */
    fun getCalendarInfo(@UnitTime type: Int, time: Long): CalendarInfo {
        val millionTime = when (type) {
            UnitTime.MILLIONSECOND -> time
            UnitTime.SECOND -> time * 1000
            UnitTime.MINUTE -> time * 1000 * 60
            UnitTime.HOUR -> time * 1000 * 3600
            else -> throw RuntimeException("UnitTime error")
        }
        val cd = Calendar.getInstance()
        cd.time = Date(millionTime)
        val year = cd.get(Calendar.YEAR)
        val month = cd.get(Calendar.MONTH)
        val day = cd.get(Calendar.DAY_OF_MONTH)
        val w = cd.get(Calendar.DAY_OF_WEEK)
        val week: Int
        week = when (w) {
            Calendar.SUNDAY -> UnitWeek.SUNDAY
            Calendar.MONDAY -> UnitWeek.MONDAY
            Calendar.TUESDAY -> UnitWeek.TUESDAY
            Calendar.WEDNESDAY -> UnitWeek.WEDNESDAY
            Calendar.THURSDAY -> UnitWeek.THURSDAY
            Calendar.FRIDAY -> UnitWeek.FRIDAY
            Calendar.SATURDAY -> UnitWeek.SATURDAY
            else -> throw RuntimeException("DAY_OF_WEEK error")
        }
        return CalendarInfo(year, month, day, week)
    }
}
