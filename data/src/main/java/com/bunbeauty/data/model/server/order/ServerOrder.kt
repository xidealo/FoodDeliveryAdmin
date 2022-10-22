package com.bunbeauty.data.model.server.order

import com.bunbeauty.data.model.server.CartProductServer
import kotlinx.serialization.Serializable

@Serializable
data class ServerOrder(
    val uuid: String = "",
    val code: String = "",
    val status: String = "NOT_ACCEPTED",
    val time: Long = 0,
    val isDelivery: Boolean = true,
    val deferredTime: Long? = null,
    val addressDescription: String = "",
    val comment: String? = null,
    val clientUser: ClientUserServer? = null,
    val cafeUuid: String = "",
    val deliveryCost: Int? = null,
    val oderProductList: List<CartProductServer> = emptyList(),
    val availableStatusList: List<String> = emptyList()
)
