package com.bunbeauty.domain.model.cafe

import kotlinx.datetime.LocalTime

data class CafeWorkingHours(
    val fromTimeText: String,
    val fromTime: LocalTime,
    val toTimeText: String,
    val toTime: LocalTime,
) {
    companion object {
        val mock =
            CafeWorkingHours(
                fromTimeText = "",
                fromTime = LocalTime(0,0,0),
                toTimeText = "",
                toTime = LocalTime(0,0,0),
            )
    }
}
