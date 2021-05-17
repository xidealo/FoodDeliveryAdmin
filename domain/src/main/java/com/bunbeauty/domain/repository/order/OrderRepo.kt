package com.bunbeauty.domain.repository.order

import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderEntity
import kotlinx.coroutines.flow.Flow

interface OrderRepo {

    suspend fun insert(orderEntity: OrderEntity): Long
    fun update(order: Order)

    fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>>
    fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>>

    fun getAllCafeOrdersByDay(): Flow<Map<String, List<Order>>>
    fun getAllCafeOrdersByWeek(): Flow<Map<String, List<Order>>>
    fun getAllCafeOrdersByMonth(): Flow<Map<String, List<Order>>>
    fun getCafeOrdersByCafeIdAndDay(cafeId: String): Flow<Map<String, List<Order>>>
    fun getCafeOrdersByCafeIdAndWeek(cafeId: String): Flow<Map<String, List<Order>>>
    fun getCafeOrdersByCafeIdAndMonth(cafeId: String): Flow<Map<String, List<Order>>>
}