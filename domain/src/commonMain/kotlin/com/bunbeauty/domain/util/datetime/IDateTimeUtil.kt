package com.bunbeauty.domain.util.datetime

import kotlinx.datetime.LocalTime

interface IDateTimeUtil {
    fun getWeekPeriod(millis: Long): String

    fun getDaySeconds(time: LocalTime): Int

    fun getDaySeconds(
        hour: Int,
        minute: Int,
    ): Int

    fun getHour(daySeconds: Int): Int

    fun getMinute(daySeconds: Int): Int

    fun getLocalTime(daySeconds: Int): LocalTime

    fun formatDateTime(
        millis: Long,
        pattern: String,
        offset: Int? = null,
    ): String

    fun getTimeHHMM(daySeconds: Int): String

    fun getTimeHHMM(localTime: LocalTime): String
}
