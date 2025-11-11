package com.bunbeauty.domain.feature.time

interface TimeService {
    fun getCurrentTime(timeZoneOffset: Int): Time

    fun getCurrentTimeMillis(): Long

    fun getCurrentDayStartMillis(timeZoneOffset: Int): Long
}
