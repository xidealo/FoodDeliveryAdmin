package com.bunbeauty.domain.util.datetime

import java.time.LocalTime

interface IDateTimeUtil {

    fun getWeekPeriod(millis: Long): String
    fun getMillisDaysAgo(days: Int): Double
    fun getDaySeconds(time: LocalTime): Int
    fun getLocalTime(daySeconds: Int): LocalTime
    fun formatDateTime(millis: Long, pattern: String, offset: Int? = null): String
    fun getTimeHHMM(daySeconds: Int): String
    fun getTimeHHMM(localTime: LocalTime): String

}