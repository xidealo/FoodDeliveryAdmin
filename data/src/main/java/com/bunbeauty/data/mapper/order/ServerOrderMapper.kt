package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.mapper.cart_product.ICartProductMapper
import com.bunbeauty.data.mapper.user_address.IUserAddressMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.data.model.server.order.ServerOrder
import javax.inject.Inject

class ServerOrderMapper @Inject constructor(
    private val userAddressMapper: IUserAddressMapper,
    private val cartProductMapper: ICartProductMapper
) : IServerOrderMapper {

    override fun toModel(order: ServerOrder): Order {
        return Order(
            uuid = order.uuid,
            cafeUuid = order.cafeUuid,
            cartProductList = order.cartProducts.map { serverCartProduct ->
                cartProductMapper.from(serverCartProduct)
            },
            address = userAddressMapper.from(order.orderEntity.address),
            code = order.orderEntity.code,
            comment = order.orderEntity.comment,
            deferred = order.orderEntity.deferred,
            delivery = order.orderEntity.delivery,
            bonus = order.orderEntity.bonus,
            email = order.orderEntity.email,
            orderStatus = OrderStatus.valueOf(order.orderEntity.orderStatus),
            phone = order.orderEntity.phone,
            time = order.timestamp,
            userId = order.orderEntity.userId,
        )
    }

}