package com.bunbeauty.domain.model.cafe.server

data class ServerCafe(
    var uuid: String = "",
    val address: ServerCafeAddress = ServerCafeAddress(),
    val cafeEntity: ServerCafeEntity = ServerCafeEntity()
)