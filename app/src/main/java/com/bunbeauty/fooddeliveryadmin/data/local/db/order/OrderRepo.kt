package com.bunbeauty.fooddeliveryadmin.data.local.db.order

import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderEntity
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import kotlinx.coroutines.Deferred

interface OrderRepo {
    suspend fun insertOrderAsync(orderEntity: OrderEntity): Deferred<OrderEntity>
    fun getOrdersWithCartProducts(): LiveData<List<Order>>
}