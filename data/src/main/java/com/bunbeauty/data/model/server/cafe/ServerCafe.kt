package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class ServerCafe(
    var uuid: String = "cafe_uuid",
    val address: ServerCafeAddress = ServerCafeAddress(),
    val cafeEntity: ServerCafeEntity = ServerCafeEntity()
)