package com.bunbeauty.data.model.server.nonworkingday

import kotlinx.serialization.Serializable

@Serializable
class PostNonWorkingDayServer(
    val timestamp: Long,
    val cafeUuid: String,
)
