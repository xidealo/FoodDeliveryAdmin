package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.statistic.Statistic
import kotlinx.coroutines.flow.Flow

interface OrderRepo {

    suspend fun updateStatus(cafeUuid: String, orderUuid: String, status: OrderStatus)

    suspend fun getOrderListByCafeId(cafeId: String): Flow<List<Order>>
}