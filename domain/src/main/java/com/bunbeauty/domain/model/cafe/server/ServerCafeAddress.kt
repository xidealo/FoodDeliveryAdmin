package com.bunbeauty.domain.model.cafe.server

data class ServerCafeAddress(
    val city: String = "",
    val street: ServerCafeStreet = ServerCafeStreet(),
    val house: String = ""
)
