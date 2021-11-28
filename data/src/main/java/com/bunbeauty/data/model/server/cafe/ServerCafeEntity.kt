package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class ServerCafeEntity(
    val coordinate: ServerCoordinate? = null,
    val fromTime: String = "",
    val toTime: String = "",
    val phone: String = "",
    val visible: Boolean = true
)
