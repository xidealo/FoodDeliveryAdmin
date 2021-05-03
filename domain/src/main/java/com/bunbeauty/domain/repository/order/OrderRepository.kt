package com.bunbeauty.domain.repository.order

import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderEntity
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val iApiRepository: IApiRepository
) : OrderRepo {

    override suspend fun insert(orderEntity: OrderEntity) = orderDao.insert(orderEntity)

    override fun update(order: Order) {
        iApiRepository.updateOrder(order)
    }
}