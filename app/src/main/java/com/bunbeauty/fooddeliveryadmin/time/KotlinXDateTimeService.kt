package com.bunbeauty.fooddeliveryadmin.time

import com.bunbeauty.domain.feature.time.Time
import com.bunbeauty.domain.feature.time.TimeService
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class KotlinXDateTimeService @Inject constructor(): TimeService {

    override fun getCurrentTime(timeZone: String): Time {
        val currentMillis = Clock.System.now().toEpochMilliseconds()
        val instant = Instant.fromEpochMilliseconds(currentMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.of(timeZone))
        return Time(
            hour = localDateTime.hour,
            minute = localDateTime.minute,
            second = localDateTime.second,
        )
    }
}