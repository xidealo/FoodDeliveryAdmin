package com.bunbeauty.domain.model.cafe

import java.time.LocalTime

data class CafeWorkingHours(
    val fromTimeText: String,
    val fromTime: LocalTime,
    val toTimeText: String,
    val toTime: LocalTime,
)