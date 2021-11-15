package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class CafeServer(
    var uuid: String = "",
    val fromTime: Int = 0,
    val toTime: Int = 0,
    val offset: Int = 0,
    val phone: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val cityUuid: String = "",
    val isVisible: Boolean = true,
)