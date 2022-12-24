package com.bunbeauty.data.model.server.order

import com.bunbeauty.data.model.server.CartProductServer
import kotlinx.serialization.Serializable

@Serializable
class OrderDetailsServer(
    val uuid: String,
    val code: String,
    val status: String,
    val time: Long,
    val timeZone: String,
    val isDelivery: Boolean,
    val deferredTime: Long?,
    val address: OrderAddressServer,
    val comment: String?,
    val clientUser: ClientUserServer,
    val cafeUuid: String,
    val deliveryCost: Int?,
    val oldTotalCost: Int?,
    val newTotalCost: Int,
    val oderProductList: List<CartProductServer>,
    val availableStatusList: List<String>
)

@Serializable
class OrderAddressServer(
    val description: String?,
    val street: String?,
    val house: String?,
    val flat: String?,
    val entrance: String?,
    val floor: String?,
    val comment: String?,
)