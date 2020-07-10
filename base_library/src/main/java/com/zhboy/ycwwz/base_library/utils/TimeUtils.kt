package com.zhboy.ycwwz.base_library.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        const val HOURS_PER_DAY = 24
        const val MINUTES_PER_HOUR = 60
        const val SECONDS_PER_MINUTE = 60
        const val MILLISECOND_PER_SECOND = 1000

        /**
         * 获取今日 凌晨时间 00:00
         */
        fun getTodayZeroTime(): Long {
            val current = System.currentTimeMillis()
            return current - (current + TimeZone.getDefault().rawOffset) % (1000 * 60 * 60 * 24)
        }


        /**
         * 日期转星期
         *
         * @param datetime 如：20190513
         * @param format   格式 eg:yyyymmdd:
         * @return 星期
         */
        fun dateToWeek(datetime: String, format: String): String {
            var newdateTime=datetime
            try {

                if(datetime.contains("T")){
                    newdateTime  = datetime.substring(0, datetime.indexOf("T"))
                    TLog.i("dateTime",newdateTime)

                }
                val f = SimpleDateFormat("yyyy-MM-dd")
                val weekDays = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
                val cal = Calendar.getInstance() // 获得一个日历
                val date: Date
                date = f.parse(newdateTime)
                cal.time = date

                var w = cal.get(Calendar.DAY_OF_WEEK) - 1 // 指示一个星期中的某天。
                if (w < 0)
                    w = 0
                return weekDays[w]
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        /**
         * 日期转星期
         *
         * @param datetime 如：20190513
         * @param format   格式 eg:yyyymmdd:
         * @return 星期
         */
        fun dateToWeekX(datetime: String, format: String): String {
            var newdateTime=datetime
            try {

                if(datetime.contains("T")){
                    newdateTime  = datetime.substring(0, datetime.indexOf("T"))
                    TLog.i("dateTime",newdateTime)

                }
                val f = SimpleDateFormat("yyyy-MM-dd")
                val weekDays = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
                val cal = Calendar.getInstance() // 获得一个日历
                val date: Date
                date = f.parse(newdateTime)
                cal.time = date

                var w = cal.get(Calendar.DAY_OF_WEEK) - 1 // 指示一个星期中的某天。
                if (w < 0)
                    w = 0
                return weekDays[w]
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}