package com.bunbeauty.domain.order

import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.statistic.ProductStatistic

interface IOrderUtil {

    fun getProceeds(orderList: List<Order>): Int
    fun getAverageCheck(orderList: List<Order>): Int
    fun getProductStatisticList(orderList: List<Order>): List<ProductStatistic>
}