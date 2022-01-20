package com.bunbeauty.domain.util.order

import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.order.Order

interface IOrderUtil {

    fun getDeliveryCost(order: Order, delivery: Delivery): Int
    fun getOldOrderCost(order: Order, delivery: Delivery): Int?
    fun getNewOrderCost(order: Order, delivery: Delivery): Int
}