package com.bunbeauty.fooddeliveryadmin.data.local.db.order

import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepo {

    override suspend fun insertOrderAsync(order: Order) = withContext(Dispatchers.IO) {
        async {
            order.id = orderDao.insert(order)
            order
        }
    }

    override fun getOrdersWithCartProducts(): LiveData<List<OrderWithCartProducts>> {
        return orderDao.getOrders()
    }
}