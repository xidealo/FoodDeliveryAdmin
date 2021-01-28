package com.bunbeauty.fooddeliveryadmin.data.local.db.order

import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderEntity
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepo {

    override suspend fun insertOrderAsync(orderEntity: OrderEntity) = withContext(Dispatchers.IO) {
        async {
            orderEntity.id = orderDao.insert(orderEntity)
            orderEntity
        }
    }

    /*override fun getOrdersWithCartProducts(): LiveData<List<Order>> {
        return orderDao.getOrders()
    }*/
}