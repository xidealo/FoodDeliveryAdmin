package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.mapper.cart_product.ICartProductMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.data.model.server.order.ServerOrder
import javax.inject.Inject

class ServerOrderMapper @Inject constructor(
    private val cartProductMapper: ICartProductMapper
) : IServerOrderMapper {

    override fun toModel(order: ServerOrder): Order {
        return Order(
            uuid = order.uuid,
            cafeUuid = order.cafeUuid,
            cartProductList = order.cartProducts.map { serverCartProduct ->
                cartProductMapper.from(serverCartProduct)
            },
            address = order.addressDescription,
            code = order.code,
            comment = order.comment,
            deferred = order.deferredTime,
            delivery = order.isDelivery,
            bonus = 0,
            email = order.clientUser?.email ?: "",
            orderStatus = OrderStatus.valueOf(order.status),
            phone = order.clientUser?.phoneNumber ?: "",
            time = order.time,
            userId = order.clientUser?.uuid ?: "",
        )
    }

}