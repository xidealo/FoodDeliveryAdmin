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
    val percentDiscount: Int?,
    val oldTotalCost: Int?,
    val newTotalCost: Int,
    val oderProductList: List<OrderProduct>,
    val availableStatusList: List<OrderStatus>
) {
    companion object {
        val mock = OrderDetails(
            uuid = "",
            code = "",
            status = OrderStatus.NOT_ACCEPTED,
            time = 0,
            timeZone = "",
            isDelivery = false,
            deferredTime = null,
            paymentMethod = null,
            address = OrderAddress(
                description = null,
                street = null,
                house = null,
                flat = null,
                entrance = null,
                floor = null,
                comment = null
            ),
            comment = null,
            clientUser = ClientUser(
                uuid = "",
                phoneNumber = "",
                email = null
            ),
            cafeUuid = "",
            deliveryCost = null,
            percentDiscount = null,
            oldTotalCost = null,
            newTotalCost = 0,
            oderProductList = emptyList(),
            availableStatusList = emptyList()
        )
    }
}