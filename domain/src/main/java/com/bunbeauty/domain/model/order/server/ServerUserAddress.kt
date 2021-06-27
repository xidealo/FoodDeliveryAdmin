package com.bunbeauty.domain.model.order.server

data class ServerUserAddress(
    val street: ServerUserStreet = ServerUserStreet(),
    val house: String = "",
    val flat: String? = null,
    val entrance: String? = null,
    val intercom: String? = null,
    val floor: String? = null,
)
