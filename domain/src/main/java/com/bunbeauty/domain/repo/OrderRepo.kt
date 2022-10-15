package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepo {

    val orderListFlow: Flow<List<Order>>

    suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus)

    suspend fun subscribeOnOrderList(token: String, cafeId: String)
    suspend fun unsubscribeOnOrderList(cafeId: String, message: String)
    suspend fun loadOrderListByCafeId(token: String, cafeId: String)

    suspend fun subscribeOnNotification(cafeId: String)
    suspend fun unsubscribeOnNotification(cafeId: String)
}