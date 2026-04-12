package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
class GetPointServer(
    val uuid: String,
    var order: Int,
    var latitude: Double,
    var longitude: Double,
    val isVisible: Boolean?,
)
