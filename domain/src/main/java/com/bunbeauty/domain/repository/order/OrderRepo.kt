package com.bunbeauty.domain.repository.order

import com.bunbeauty.data.model.order.OrderEntity
import kotlinx.coroutines.Deferred

interface OrderRepo {
    suspend fun insertOrderAsync(orderEntity: OrderEntity): Deferred<OrderEntity>
}