package com.bunbeauty.data.mapper.order

import com.bunbeauty.data.mapper.cart_product.ICartProductMapper
import com.bunbeauty.data.mapper.user_address.IUserAddressMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.server.ServerOrder
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import javax.inject.Inject

class ServerOrderMapper @Inject constructor(
    private val userAddressMapper: IUserAddressMapper,
    private val cartProductMapper: ICartProductMapper
) : IServerOrderMapper {

    override fun from(model: ServerOrder): Order {
        return Order(
            uuid = model.uuid,
            cafeUuid = model.cafeUuid,
            cartProductList = model.cartProducts.map { serverCartProduct ->
                cartProductMapper.from(serverCartProduct)
            },
            address = userAddressMapper.from(model.orderEntity.address),
            code = model.orderEntity.code,
            comment = model.orderEntity.comment,
            deferred = model.orderEntity.deferredTime,
            delivery = model.orderEntity.delivery,
            bonus = model.orderEntity.bonus,
            email = model.orderEntity.email,
            orderStatus = OrderStatus.valueOf(model.orderEntity.orderStatus),
            phone = model.orderEntity.phone,
            time = model.timestamp,
            userId = model.orderEntity.userId,
        )
    }

    override fun to(model: Order): ServerOrder {
        return ServerOrder()
    }
}