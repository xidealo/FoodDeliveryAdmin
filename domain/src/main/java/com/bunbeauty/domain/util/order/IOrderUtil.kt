package com.bunbeauty.domain.util.order

import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.statistic.ProductStatistic

interface IOrderUtil {

    fun getDeliveryCost(order: Order, delivery: Delivery): Int
    fun getOldOrderCost(order: Order, delivery: Delivery): Int?
    fun getNewOrderCost(order: Order, delivery: Delivery): Int
}