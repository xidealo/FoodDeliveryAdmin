package com.bunbeauty.domain.model.order.details

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.cartproduct.OrderProduct

data class OrderDetails(
    val uuid: String,
    val code: String,
    val status: OrderStatus,
    val time: Long,
    val timeZone: String,
    val isDelivery: Boolean,
    val deferredTime: Long?,
    val paymentMethod: PaymentMethod?,
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