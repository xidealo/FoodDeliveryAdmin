package com.bunbeauty.domain.order

import com.bunbeauty.data.model.order.Order

interface IOrderUtil {

    fun getProceeds(orderList: List<Order>): Int
    fun getAverageCheck(orderList: List<Order>): Int
}