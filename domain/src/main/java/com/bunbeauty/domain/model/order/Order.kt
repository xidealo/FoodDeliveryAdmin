package com.bunbeauty.domain.model.order

import com.bunbeauty.domain.enums.OrderStatus

data class Order(
    val uuid: String,
    val code: String,
    val time: Long,
    val deferredTime: Long?,
    val timeZone: String,
    val orderStatus: OrderStatus
)
