package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderDetails
import com.bunbeauty.domain.model.order.OrderError
import com.bunbeauty.domain.model.order.OrderListResult
import kotlinx.coroutines.flow.Flow

interface OrderRepo {
    val orderListFlow: Flow<OrderListResult>

    suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus)
    suspend fun getOrderListFlow(token: String, cafeUuid: String): Flow<List<Order>>
    suspend fun getOrderErrorFlow(token: String, cafeUuid: String): Flow<OrderError>
    suspend fun loadOrderByUuid(token: String, orderUuid: String): OrderDetails?
}