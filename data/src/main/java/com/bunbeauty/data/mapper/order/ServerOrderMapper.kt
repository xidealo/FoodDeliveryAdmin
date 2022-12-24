package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.mapper.CartProductMapper
import com.bunbeauty.data.model.server.order.OrderDetailsServer
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.ClientUser
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderAddress
import com.bunbeauty.domain.model.order.OrderDetails
import javax.inject.Inject

class ServerOrderMapper @Inject constructor(
    private val cartProductMapper: CartProductMapper
) : IServerOrderMapper {

    override fun toModel(order: OrderDetailsServer): OrderDetails {
        return OrderDetails(
            uuid = order.uuid,
            code = order.code,
            status = getOrderStatus(order.status),
            time = order.time,
            timeZone = order.timeZone,
            isDelivery = order.isDelivery,
            deferredTime = order.deferredTime,
            address = OrderAddress(
                description = order.address.description,
                street = order.address.street,
                house = order.address.house,
                flat = order.address.flat,
                entrance = order.address.entrance,
                floor = order.address.floor,
                comment = order.address.comment,
            ),
            comment = order.comment,
            clientUser = ClientUser(
                uuid = order.clientUser.uuid,
                phoneNumber = order.clientUser.phoneNumber,
                email = order.clientUser.email,
            ),
            cafeUuid = order.cafeUuid,
            deliveryCost = order.deliveryCost,
            oldTotalCost = order.oldTotalCost,
            newTotalCost = order.newTotalCost,
            oderProductList = order.oderProductList.map(cartProductMapper::toModel),
            availableStatusList = order.availableStatusList.mapNotNull(::getOrderStatusNullable),
        )
    }

    override fun toModel(order: ServerOrder): Order {
        return Order(
            uuid = order.uuid,
            code = order.code,
            time = order.time,
            deferredTime = order.deferredTime,
            timeZone = order.timeZone,
            orderStatus = getOrderStatus(order.status),
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