package com.bunbeauty.domain.repository.order

import com.bunbeauty.data.model.order.OrderEntity
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
}