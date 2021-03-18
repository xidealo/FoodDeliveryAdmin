package com.bunbeauty.fooddeliveryadmin.data.local.db.order

import com.bunbeauty.data.model.order.OrderEntity
import kotlinx.coroutines.Deferred

interface OrderRepo {
    suspend fun insertOrderAsync(orderEntity: OrderEntity): Deferred<OrderEntity>
    //fun getOrdersWithCartProducts(): LiveData<List<Order>>
}