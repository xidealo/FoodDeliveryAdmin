package com.bunbeauty.domain.repository.order

import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderEntity

interface OrderRepo {
    suspend fun insert(orderEntity: OrderEntity): Long
    fun update(order: Order)
}