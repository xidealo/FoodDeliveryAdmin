package com.bunbeauty.fooddeliveryadmin.screen.order_list.list

import com.bunbeauty.domain.enums.OrderStatus

data class OrderItemModel(
    val uuid: String,
    val status: OrderStatus,
    val statusString: String,
    val code: String,
    val deferredTime: String,
    val time: String
)
