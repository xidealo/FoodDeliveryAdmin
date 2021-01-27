package com.bunbeauty.fooddeliveryadmin.utils.string

import com.bunbeauty.fooddeliveryadmin.data.model.Address
import com.bunbeauty.fooddeliveryadmin.data.model.CartProduct
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderEntity

interface IStringHelper {
    fun toString(address: Address): String
    fun toString(orderEntity: OrderEntity): String
    fun toString(cartProducts: List<CartProduct>): String
}