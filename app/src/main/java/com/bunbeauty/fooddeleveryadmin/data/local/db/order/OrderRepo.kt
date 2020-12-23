package com.bunbeauty.fooddeleveryadmin.data.local.db.order

import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeleveryadmin.data.model.order.Order
import com.bunbeauty.fooddeleveryadmin.data.model.order.OrderWithCartProducts
import kotlinx.coroutines.Deferred

interface OrderRepo {
    suspend fun insertOrderAsync(order: Order): Deferred<Order>
    fun getOrdersWithCartProducts(): LiveData<List<OrderWithCartProducts>>
}