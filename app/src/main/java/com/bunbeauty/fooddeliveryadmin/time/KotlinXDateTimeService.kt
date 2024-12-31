package com.bunbeauty.fooddeliveryadmin.time

import com.bunbeauty.domain.feature.time.Time
import com.bunbeauty.domain.feature.time.TimeService
import kotlinx.datetime.Clock
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

class KotlinXDateTimeService : TimeService {

    override fun getCurrentTime(timeZoneOffset: Int): Time {
        val instant = Clock.System.now()
        val timeZone = UtcOffset(hours = timeZoneOffset).asTimeZone()
        val localDateTime = instant.toLocalDateTime(timeZone)

        return Time(
            hour = localDateTime.hour,
            minute = localDateTime.minute,
            second = localDateTime.second
        )
    }

    override fun getCurrentTimeMillis(): Long {
        val instant = Clock.System.now()

        return instant.toEpochMilliseconds()
    }

    override fun getCurrentDayStartMillis(timeZoneOffset: Int): Long {
        val timeZone = UtcOffset(hours = timeZoneOffset).asTimeZone()
        val startDay = Clock.System.todayIn(timeZone)
            .atTime(0, 0, 0)
            .toInstant(timeZone)

        return startDay.toEpochMilliseconds()
    }
}
