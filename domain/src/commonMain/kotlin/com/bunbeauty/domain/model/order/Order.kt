package com.bunbeauty.domain.model.order

import com.bunbeauty.domain.enums.OrderStatus

data class Order(
    val uuid: String,
    val code: String,
    val time: Long,
    val deferredTime: Long?,
    val timeZone: String,
    val orderStatus: OrderStatus,
) {
    companion object {
        val mock =
            Order(
                uuid = "",
                code = "",
                time = 0,
                deferredTime = null,
                timeZone = "",
                orderStatus = OrderStatus.NOT_ACCEPTED,
            )
    }
}
