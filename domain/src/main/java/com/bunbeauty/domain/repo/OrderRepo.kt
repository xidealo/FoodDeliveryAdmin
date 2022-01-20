package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import kotlinx.coroutines.flow.MutableSharedFlow

interface OrderRepo {

    val ordersMapFlow: MutableSharedFlow<List<Order>>

    suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus)

    suspend fun subscribeOnOrderListByCafeId(token: String, cafeId: String)
    suspend fun unsubscribeOnOrderList(cafeId: String)
    suspend fun loadOrderListByCafeId(token: String, cafeId: String)
}