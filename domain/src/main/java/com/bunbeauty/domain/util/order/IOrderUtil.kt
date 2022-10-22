package com.bunbeauty.domain.util.order

import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.order.Order

interface IOrderUtil {
    fun getOldOrderCost(order: Order): Int?
    fun getNewOrderCost(order: Order): Int
}