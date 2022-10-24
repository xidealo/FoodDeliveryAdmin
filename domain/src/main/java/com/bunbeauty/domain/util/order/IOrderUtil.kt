package com.bunbeauty.domain.util.order

import com.bunbeauty.domain.model.order.OrderDetails

interface IOrderUtil {
    fun getOldOrderCost(order: OrderDetails): Int?
    fun getNewOrderCost(order: OrderDetails): Int
}