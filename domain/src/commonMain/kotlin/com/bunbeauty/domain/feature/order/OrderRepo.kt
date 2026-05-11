package com.bunbeauty.domain.feature.order

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.details.OrderDetails
import kotlinx.coroutines.flow.Flow

interface OrderRepo {
    suspend fun updateStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus,
    )

    suspend fun getOrderUpdatesStream(
        token: String,
        cafeUuid: String,
    ): Flow<OrderUpdatesStreamEvent>

    suspend fun unsubscribeOrderUpdates(message: String)

    suspend fun loadOrderByUuid(
        token: String,
        orderUuid: String,
    ): OrderDetails?

    fun clearCache()
}
