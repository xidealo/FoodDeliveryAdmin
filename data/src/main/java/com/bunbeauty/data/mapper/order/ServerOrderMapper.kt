package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.mapper.CartProductMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.data.model.server.order.ServerOrder
import javax.inject.Inject

class ServerOrderMapper @Inject constructor(
    private val cartProductMapper: CartProductMapper
) : IServerOrderMapper {

    override fun toModel(order: ServerOrder): Order {
        return Order(
            uuid = order.uuid,
            cafeUuid = order.cafeUuid,
            oderProductList = order.oderProductList.map(cartProductMapper::toModel),
            address = order.addressDescription,
            code = order.code,
            comment = order.comment,
            deferred = order.deferredTime,
            delivery = order.isDelivery,
            discount = 0,
            email = order.clientUser?.email ?: "",
            orderStatus = OrderStatus.valueOf(order.status),
            phone = order.clientUser?.phoneNumber ?: "",
            time = order.time,
            userId = order.clientUser?.uuid ?: "",
            availableStatusList = order.availableStatusList.mapNotNull { status ->
                val hasStatus = OrderStatus.values().any { orderStatus ->
                    orderStatus.name == status
                }
                if (hasStatus) {
                    OrderStatus.valueOf(status)
                } else {
                    null
                }
            }
        )
    }

}