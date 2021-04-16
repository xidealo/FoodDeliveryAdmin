package com.bunbeauty.domain.string_helper

import com.bunbeauty.data.model.Address
import com.bunbeauty.data.model.CartProduct
import com.bunbeauty.data.model.Statistic
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderEntity

interface IStringHelper {
    fun toString(address: Address): String
    fun toString(orderEntity: OrderEntity): String
    fun toStringDeferred(orderEntity: OrderEntity): String
    fun toString(cartProducts: List<CartProduct>): String
    fun toString(statistic: Statistic): String
    fun toStringCost(statistic: Statistic): String
    fun toStringCost(order: Order): String
    fun toStringOrdersCount(statistic: Statistic): String
    fun toStringTime(orderEntity: OrderEntity): String
    fun toStringTime(order: Order): String
    fun toStringCost(cost: Int): String
}