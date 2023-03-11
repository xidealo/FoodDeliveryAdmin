package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.OrderListResult
import kotlinx.coroutines.flow.Flow

interface OrderRepo {
    val orderListFlow: Flow<OrderListResult>

    suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus)

    suspend fun subscribeOnOrderList(token: String, cafeId: String)
    suspend fun unsubscribeOnOrderList(message: String)
}