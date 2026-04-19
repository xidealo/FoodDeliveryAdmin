package com.bunbeauty.domain.feature.time

import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class KotlinXDateTimeService : TimeService {
    override fun getCurrentTime(timeZoneOffset: Int): Time {
        val instant = Clock.System.now()
        val timeZone = UtcOffset(hours = timeZoneOffset).asTimeZone()
        val localDateTime = instant.toLocalDateTime(timeZone)

        return Time(
            hour = localDateTime.hour,
            minute = localDateTime.minute,
            second = localDateTime.second,
        )
    }

    override fun getCurrentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()

    override fun getCurrentDayStartMillis(timeZoneOffset: Int): Long {
        val timeZone = UtcOffset(hours = timeZoneOffset).asTimeZone()
        val startDay =
            Clock.System
                .todayIn(timeZone)
                .atTime(0, 0, 0)
                .toInstant(timeZone)

        return startDay.toEpochMilliseconds()
    }
}
