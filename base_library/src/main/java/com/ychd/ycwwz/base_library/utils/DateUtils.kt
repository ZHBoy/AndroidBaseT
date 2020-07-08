package com.ychd.ycwwz.base_library.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DateUtils {
    companion object {
        val instance: DateUtils by lazy { DateUtils() }
    }


    val DAY = 86400000 //１天＝24*60*60*1000ms
    val HOUR = 3600000
    val MIN = 60000


    /**
     * 获取某个月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    fun getMonthDays(y: Int, m: Int): Int {
        var year = y
        var month = m
        if (month > 12) {
            month = 1
            year += 1
        } else if (month < 1) {
            month = 12
            year -= 1
        }
        val arr = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var days = 0

        if (isLeapYear(year)) {
            arr[1] = 29 // 闰年2月29天
        }

        try {
            days = arr[month - 1]
        } catch (e: Exception) {
            e.stackTrace
        }

        return days
    }

    /**
     * 是否为闰年
     *
     * @param year
     * @return
     */
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }


    /**
     * 获取当前系统时间的年份
     *
     * @return
     */
    fun getYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    /**
     * 获取当前系统时间的月份
     *
     * @return
     */
    fun getMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    /**
     * 获取当前系统时间的月份的第几天
     *
     * @return
     */
    fun getCurrentMonthDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 获取当前系统时间的小时数
     *
     * @return
     */
    fun getHour(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    /**
     * 获取当前系统时间的分钟数
     *
     * @return
     */
    fun getMinute(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }

    /**
     * 获取当前系统时间的秒数
     *
     * @return
     */
    fun getSecond(): Int {
        return Calendar.getInstance().get(Calendar.SECOND)
    }

    /**
     * 获取日期年份
     *
     * @param date 日期
     * @return
     */
    fun getYear(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }

    /**
     * 获取日期月份
     *
     * @param date 日期
     * @return
     */
    fun getMonth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MONTH) + 1
    }

    /**
     * 获取日期天
     *
     * @param date 日期
     * @return
     */
    fun getDay(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 功能描述：返回小
     *
     * @param date 日期
     * @return 返回小时
     */
    fun getHour(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    fun getMinute(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MINUTE)
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    fun getSecond(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.SECOND)
    }


    /**
     * 获取当前系统时间的毫秒数
     *
     * @return
     */
    fun getMillSecond(): Int {
        return Calendar.getInstance().get(Calendar.MILLISECOND)
    }


    /**
     * 根据系统默认时区，获取当前时间与time的天数差
     *
     * @param time 相差的天数
     * @return　等于０表示今天，大于０表示今天之前
     */
    fun getDaySpan(time: Long): Long {
        return getTimeSpan(time, DAY.toLong())
    }

    fun getHourSpan(time: Long): Long {
        return getTimeSpan(time, HOUR.toLong())
    }

    fun getMinSpan(time: Long): Long {
        return getTimeSpan(time, MIN.toLong())
    }

    fun getTimeSpan(time: Long, span: Long): Long {
        // 系统默认时区，ms
        val tiemzone = TimeZone.getDefault().rawOffset
        return (System.currentTimeMillis() + tiemzone) / span - (time + tiemzone) / span
    }

    fun isToday(time: Long): Boolean {
        return getDaySpan(time) == 0L
    }

    fun isYestoday(time: Long): Boolean {
        return getDaySpan(time) == 1L
    }

    /**
     * @return 返回当前时间，yyyy-MM-dd HH-mm-ss
     */
    fun getDate(): String {
        return getDate("yyyy-MM-dd HH:mm:ss")
    }

    fun getDateyyyMMdd(): String {
        return getDate("yyyy-MM-dd")
    }


    fun getDate(format: String): String {
        return getDate(java.util.Date().time, format)
    }

    fun getDate(time: Long, format: String): String {
        val sDateFormat = SimpleDateFormat(format)
        return sDateFormat.format(time)
    }

    /**
     * 根据毫秒时间戳来格式化字符串
     * 今天显示几时几分、昨天显示昨天、前天显示前天.
     * 早于前天的显示具体年-月-日，如2017-06-12；
     *
     * @param timeStamp 毫秒值
     * @return 几时几分 昨天 前天 或者 yyyy-MM-dd 类型字符串
     */
    fun format(timeStamp: Long): String? {
        var strTime: String? = ""
        val curTimeMillis = System.currentTimeMillis()
        val todayHoursSeconds = getHour() * 60 * 60
        val todayMinutesSeconds = getMinute() * 60
        val todaySeconds = getSecond()
        val todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000
        val todayStartMillis = curTimeMillis - todayMillis
        if (timeStamp >= todayStartMillis) {
            val date = Date(timeStamp)
            strTime = getHour(date).toString() + ":" + getMinute(date)//显示几时几分
            return strTime
        }
        val oneDayMillis = 24 * 60 * 60 * 1000
        val yesterdayStartMillis = todayStartMillis - oneDayMillis
        if (timeStamp >= yesterdayStartMillis) {
            return "昨天"
        }
        val yesterdayBeforeStartMillis = yesterdayStartMillis - oneDayMillis
        if (timeStamp >= yesterdayBeforeStartMillis) {
            return "前天"
        }
        val date = Date(timeStamp)
        if (getYear() == getYear(date)) {
            strTime = getDayWithPattern(timeStamp)
        } else {
            strTime = getDateWithPattern(timeStamp)
        }
        return strTime
    }

    /**
     * 时间戳转日期
     *
     * @param time
     * @return
     */
    fun getDateWithPattern(time: Long): String? {
        if (time == 0L) return null
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date(time))
    }

    /**
     * 时间戳转年月
     *
     * @param time
     * @return
     */
    fun getYearAndMonthWithPattern(time: Long): String? {
        if (time == 0L) return null
        val sdf = SimpleDateFormat("yyyy-MM")
        return sdf.format(Date(time))
    }

    /**
     * 时间戳转日期
     *
     * @param time
     * @return
     */
    fun getDayWithPattern(time: Long): String? {
        if (time == 0L) return null
        val sdf = SimpleDateFormat("MM-dd")
        return sdf.format(Date(time))
    }

    /**
     * 时间戳转日期
     *
     * @param time
     * @return
     */
    fun getDayWithPattern(time: Long, format: String): String? {
        if (time == 0L) return null
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(time))
    }

    /**
     * 通过String获得时间戳
     *
     * @param time 20180314155644
     * @return
     */
    fun getDateByString(time: String): Long {
        val format = SimpleDateFormat("yyyyMMddHHmmss")
        var date: java.util.Date? = null
        try {
            date = format.parse(time)
            return date!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
            return 0
        }

    }

    /**
     * 通过String获得时间戳
     *
     * @param time 20180314155644
     * @return
     */
    fun getDateByString(time: String, f: String): Long {
        val format = SimpleDateFormat(f)
        var date: java.util.Date? = null
        try {
            date = format.parse(time)
            return date!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
            return 0
        }

    }


    fun getAMorPM(time: String): String {
        if (TextUtils.isEmpty(time)) return ""
        if ("AM" == time) {
            return "上午"
        }
        return if ("PM" == time) {
            "下午"
        } else ""
    }

    fun getAMorPMOfString(time: String): String {
        if (TextUtils.isEmpty(time)) return ""
        if ("上午" == time) {
            return "AM"
        }
        return if ("下午" == time) {
            "PM"
        } else ""
    }

    fun getDateCha(start: String, end: String): Int? {
        val format = SimpleDateFormat("yyyy-MM-dd")
        try {
            val startDate = format.parse(start)
            val endDate = format.parse(end)
            return differentDays(startDate, endDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return -1
    }


    /**
     * String 转Date
     *
     * @param start
     * @param f
     * @return
     */
    fun getDateTime(start: String, f: String): Date? {
        val format = SimpleDateFormat(f)
        var data: Date? = null
        try {
            data = format.parse(start)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return data
    }


    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    fun differentDays(date1: Date, date2: Date): Int {
        val cal1 = Calendar.getInstance()
        cal1.time = date1

        val cal2 = Calendar.getInstance()
        cal2.time = date2
        val day1 = cal1.get(Calendar.DAY_OF_YEAR)
        val day2 = cal2.get(Calendar.DAY_OF_YEAR)

        val year1 = cal1.get(Calendar.YEAR)
        val year2 = cal2.get(Calendar.YEAR)
        if (year1 != year2)
        //同一年
        {
            var timeDistance = 0
            for (i in year1 until year2) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)
                //闰年
                {
                    timeDistance += 366
                } else
                //不是闰年
                {
                    timeDistance += 365
                }
            }

            return timeDistance + (day2 - day1)
        } else
        //不同年
        {
            return day2 - day1
        }
    }

    /**
     * 时间戳转日期
     *
     * @param time
     * @return
     */
    fun getSFMDate(time: Long): String? {
        if (time == 0L) return null
        val sdf = SimpleDateFormat("hh:mm")
        return sdf.format(Date(time))
    }


    /*截取时间中的小时和分钟*/
    fun getHourMinByDate(data: String?): String {
        if (data.isNullOrBlank()) return ""
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: java.util.Date? = null
        try {
            date = format.parse(data)
            val c = Calendar.getInstance()
            c.time = date
            return c.get(Calendar.HOUR_OF_DAY).toString() + ":" + c.get(java.util.Calendar.MINUTE)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }

    }

    /*当前是否为夜晚*/
    fun isNight(): Boolean {
        val sdf = SimpleDateFormat("HH")
        val hour = sdf.format(Date())
        val k = hour.toInt()
        return (k in 0..5) || (k in 18..23)
    }

    /**
     * 将秒转换成分钟
     */
    fun formatTimeS(seconds: Long): String {
        var temp = 0
        val sb = StringBuffer()
        if (seconds > 3600) {
            temp = (seconds / 3600).toInt()
            sb.append(if (seconds / 3600 < 10) "0$temp:" else "$temp:")
            temp = (seconds % 3600 / 60).toInt()
            changeSeconds(seconds, temp, sb)
        } else {
            temp = (seconds % 3600 / 60).toInt()
            changeSeconds(seconds, temp, sb)
        }
        return sb.toString()
    }

    private fun changeSeconds(seconds: Long, temp: Int, sb: StringBuffer) {
        var temp = temp
        sb.append(if (temp < 10) "0$temp:" else "$temp:")
        temp = (seconds % 3600 % 60).toInt()
        sb.append(if (temp < 10) "0$temp" else "" + temp)
    }


    /**
     * 点击阅读时间后的时间展示
     */
    fun getReadTimeClick(time: String): String {
        var m = time.split(":")[0]
        m = when {
            m == "00" -> ""
            m[0].toString() == "0" -> m[1] + "分"
            else -> m + "分"
        }

        var s = time.split(":")[1]
        s = if (s[0].toString() == "0") {
            s[1] + "秒"
        } else {
            s + "秒"
        }

        return "在${m + s}后可获得1次抽奖机会"

    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private fun getPastDate(past: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past - 1)
        val today = calendar.time
        val format = SimpleDateFormat("MM.dd")
        return format.format(today)
    }

    /**
     * 获取过去或者未来 任意天内的日期数组
     * @param intervals      intervals天内
     * @return              日期数组
     */
    fun getSevenPastDays(intervals: Int): ArrayList<String> {
        val pastDaysList = ArrayList<String>()
        for (i in 0 until intervals) {
            pastDaysList.add(getPastDate(i))
        }
        //倒序列表
        pastDaysList.reverse()
        return pastDaysList
    }

    /**
     * 返回日期加X天后的日期
     */
    fun addDay(date: String, i: Int): String {
        try {
            val gCal = GregorianCalendar(
                Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10))
            )
            gCal.add(GregorianCalendar.DATE, i)
            val date_format = SimpleDateFormat("yyyy-MM-dd")
            return date_format.format(gCal.time)
        } catch (e: Exception) {
            return getDate()
        }

    }

    /**
     * 返回日期加X月后的日期
     */
    fun addMonth(date: String, i: Int): String {
        try {
            val gCal = GregorianCalendar(
                Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10))
            )
            gCal.add(GregorianCalendar.MONTH, i)
            val date_format = SimpleDateFormat("yyyy-MM-dd")
            return date_format.format(gCal.time)
        } catch (e: Exception) {
            return getDate()
        }

    }

    /*
     * 毫秒这算出天时分秒
     */
    fun getDhmsTime(ms: Long?): String {
        val ss = 1000
        val mi = ss * 60
        val hh = mi * 60
        val dd = hh * 24

        val day = ms!! / dd
        val hour = (ms - day * dd) / hh
        val minute = (ms - day * dd - hour * hh) / mi
        val second = (ms - day * dd - hour * hh - minute * mi) / ss

        val sb = StringBuffer()
        if (day > 0) {
            sb.append(day.toString() + "天")
        }
        if (hour > 0) {
            sb.append(hour.toString() + "小时")
        }
        if (minute > 0) {
            sb.append(minute.toString() + "分")
        }
        if (second > 0) {
            sb.append(second.toString() + "秒")
        }

        return sb.toString()
    }

    /*
     * 毫秒这算出天时分秒
     */
    fun getHmsTime(ms: Long?): List<String> {
        val ss = 1000
        val mi = ss * 60
        val hh = mi * 60
        val dd = hh * 24

        val day = ms!! / dd
        val hour = (ms - day * dd) / hh
        val minute = (ms - day * dd - hour * hh) / mi
        val second = (ms - day * dd - hour * hh - minute * mi) / ss

        val sumHour = hour + day * 24

        val array = ArrayList<String>(3)
        val h = if (sumHour > 9) {
            sumHour.toString()
        } else {
            "0$sumHour"
        }
        array.add(h)

        val m = if (minute > 9) {
            minute.toString()
        } else {
            "0$minute"
        }
        array.add(m)
        val s = if (second > 9) {
            second.toString()
        } else {
            "0$second"
        }
        array.add(s)
        return array
    }

    /**
     * 将毫秒数转换为分秒字符串
     */
    fun formatSecondsToTimeString(date: Long?): String {
        var strtime = ""
        var minStr = ""
        var sStr = ""
        val day = date!! / (60 * 60 * 24)
        val hour = date / (60 * 60) - day * 24
        val min = date / (60) - day * 24 * 60 - hour * 60
        val s = date - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60

        if (min < 10 && s >= 10) {
            minStr = "0$min"
            sStr = s.toString() + ""
        }
        if (min < 10 && s < 10) {
            minStr = "0$min"
            sStr = "0$s"
        }
        if (min >= 10 && s >= 10) {
            minStr = min.toString() + ""
            sStr = s.toString() + ""
        }
        if (min >= 10 && s < 10) {
            minStr = min.toString() + ""
            sStr = "0$s"
        }
        strtime = "$minStr:$sStr"
        return strtime
    }
}