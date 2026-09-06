package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class CafeWorkingDayServer(
    val dayOfWeek: Int,
    val fromTime: Int,
    val toTime: Int,
)
