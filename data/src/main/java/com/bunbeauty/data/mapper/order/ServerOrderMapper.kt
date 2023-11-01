package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.mapper.CartProductMapper
import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.order.OrderServer
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.details.ClientUser
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.details.OrderAddress
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.model.order.details.PaymentMethod
import javax.inject.Inject

class ServerOrderMapper @Inject constructor(
    private val cartProductMapper: CartProductMapper
) : IServerOrderMapper {

    override fun mapOrderDetails(orderDetailsServer: OrderDetailsServer): OrderDetails {
        return OrderDetails(
            uuid = orderDetailsServer.uuid,
            code = orderDetailsServer.code,
            status = getOrderStatus(orderDetailsServer.status),
            time = orderDetailsServer.time,
            timeZone = orderDetailsServer.timeZone,
            isDelivery = orderDetailsServer.isDelivery,
            deferredTime = orderDetailsServer.deferredTime,
            paymentMethod = PaymentMethod.values().firstOrNull { paymentMethod  ->
                paymentMethod.name == orderDetailsServer.paymentMethod
            },
            address = OrderAddress(
                description = orderDetailsServer.address.description,
                street = orderDetailsServer.address.street,
                house = orderDetailsServer.address.house,
                flat = orderDetailsServer.address.flat,
                entrance = orderDetailsServer.address.entrance,
                floor = orderDetailsServer.address.floor,
                comment = orderDetailsServer.address.comment,
            ),
            comment = orderDetailsServer.comment,
            clientUser = ClientUser(
                uuid = orderDetailsServer.clientUser.uuid,
                phoneNumber = orderDetailsServer.clientUser.phoneNumber,
                email = orderDetailsServer.clientUser.email,
            ),
            cafeUuid = orderDetailsServer.cafeUuid,
            deliveryCost = orderDetailsServer.deliveryCost,
            percentDiscount = orderDetailsServer.percentDiscount,
            oldTotalCost = orderDetailsServer.oldTotalCost,
            newTotalCost = orderDetailsServer.newTotalCost,
            oderProductList = orderDetailsServer.oderProductList.map(cartProductMapper::toModel),
            availableStatusList = orderDetailsServer.availableStatusList.mapNotNull(::getOrderStatusNullable),
        )
    }

    override fun mapOrder(orderServer: OrderServer): Order {
        return Order(
            uuid = orderServer.uuid,
            code = orderServer.code,
            time = orderServer.time,
            deferredTime = orderServer.deferredTime,
            timeZone = orderServer.timeZone,
            orderStatus = getOrderStatus(orderServer.status),
        )
    }

    private fun getOrderStatus(statusName: String): OrderStatus {
        return getOrderStatusNullable(statusName) ?: OrderStatus.NOT_ACCEPTED
    }

    private fun getOrderStatusNullable(statusName: String): OrderStatus? {
        val hasStatus = OrderStatus.values().any { orderStatus ->
            orderStatus.name == statusName
        }
        return if (hasStatus) {
            OrderStatus.valueOf(statusName)
        } else {
            null
        }
    }

}