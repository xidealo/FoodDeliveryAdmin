package com.bunbeauty.presentation.model

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order

data class OrderItemModel(
    val status: OrderStatus,
    val statusString: String,
    val code: String,
    val deferredTime: String,
    val time: String,
    val order: Order,
)
