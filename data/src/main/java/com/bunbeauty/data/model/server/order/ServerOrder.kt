package com.bunbeauty.data.model.server.order

import com.bunbeauty.data.model.server.ServerCartProduct
import kotlinx.serialization.Serializable

@Serializable
data class ServerOrder(
    var uuid: String = "",
    val code: String = "",
    val status: String = "NOT_ACCEPTED",
    val time: Long = 0,
    val isDelivery: Boolean = true,
    val deferredTime: String? = null,
    val addressDescription: String = "",
    val comment: String? = null,
    val clientUser: ClientUserServer? = null,
    val cartProducts: List<ServerCartProduct> = emptyList(),
    var cafeUuid: String = "",
)
