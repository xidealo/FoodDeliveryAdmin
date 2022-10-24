package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.mapper.CartProductMapper
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.data.model.server.order.ServerOrderDetails
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderDetails
import javax.inject.Inject

class ServerOrderMapper @Inject constructor(
    private val cartProductMapper: CartProductMapper
) : IServerOrderMapper {

    override fun toModel(order: ServerOrderDetails): OrderDetails {
        return OrderDetails(
            uuid = order.uuid,
            cafeUuid = order.cafeUuid,
            oderProductList = order.oderProductList.map(cartProductMapper::toModel),
            address = order.addressDescription,
            code = order.code,
            comment = order.comment,
            deferred = order.deferredTime,
            delivery = order.isDelivery,
            discount = null,
            email = order.clientUser?.email ?: "",
            orderStatus = getOrderStatus(order.status),
            phone = order.clientUser?.phoneNumber ?: "",
            time = order.time,
            userId = order.clientUser?.uuid ?: "",
            deliveryCost = order.deliveryCost,
            availableStatusList = order.availableStatusList.mapNotNull { status ->
                getOrderStatusNullable(status)
            }
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