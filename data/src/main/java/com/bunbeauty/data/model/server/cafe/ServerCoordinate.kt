package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class ServerCoordinate(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
