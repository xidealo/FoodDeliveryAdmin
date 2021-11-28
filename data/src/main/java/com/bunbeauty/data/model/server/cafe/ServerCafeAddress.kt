package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class ServerCafeAddress(
    val city: String = "",
    val street: ServerCafeStreet = ServerCafeStreet(),
    val house: String = ""
)
