package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class CafeServer(
    var uuid: String,
    val fromTime: Int,
    val toTime: Int,
    val offset: Int = 0,
    val phone: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val cityUuid: String,
    val isVisible: Boolean,
    val workType: String,
    val workload: String
)
