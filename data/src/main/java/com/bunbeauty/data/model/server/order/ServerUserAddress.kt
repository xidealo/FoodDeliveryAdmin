package com.bunbeauty.data.model.server.order

import kotlinx.serialization.Serializable

@Serializable
data class ServerUserAddress(
    val street: ServerUserStreet = ServerUserStreet(),
    val house: String = "",
    val flat: String? = null,
    val entrance: String? = null,
    val comment: String? = null,
    val floor: String? = null,
)
