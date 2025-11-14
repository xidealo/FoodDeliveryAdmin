package com.bunbeauty.data.model.server.nonworkingday

import kotlinx.serialization.Serializable

@Serializable
class NonWorkingDayServer(
    val uuid: String,
    val timestamp: Long,
    val cafeUuid: String,
    val isVisible: Boolean,
)
