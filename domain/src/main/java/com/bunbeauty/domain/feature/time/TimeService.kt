package com.bunbeauty.domain.feature.time

interface TimeService {

    fun getCurrentTime(timeZone: String): Time
}