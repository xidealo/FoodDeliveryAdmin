package com.bunbeauty.domain.model.order

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.cart_product.OrderProduct

data class OrderDetails(
    val uuid: String,
    val code: String,
    val status: OrderStatus,
    val time: Long,
    val timeZone: String,
    val isDelivery: Boolean,
    val deferredTime: Long?,
    val address: OrderAddress,
    val comment: String?,
    val clientUser: ClientUser,
    val cafeUuid: String,
    val deliveryCost: Int?,
    val oldTotalCost: Int?,
    val newTotalCost: Int,
    val oderProductList: List<OrderProduct>,
    val availableStatusList: List<OrderStatus>
)

class ClientUser(
    val uuid: String,
    val phoneNumber: String,
    val email: String?,
)

class OrderAddress(
    val description: String?,
    val street: String?,
    val house: String?,
    val flat: String?,
    val entrance: String?,
    val floor: String?,
    val comment: String?,
)