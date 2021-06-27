package com.bunbeauty.domain.model.cafe.server

data class ServerCafeEntity(
    val coordinate: ServerCoordinate? = null,
    val fromTime: String = "",
    val toTime: String = "",
    val phone: String = "",
    val visible: Boolean = true
)
