package com.bunbeauty.domain.repo

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.statistic.Statistic
import kotlinx.coroutines.flow.Flow

interface OrderRepo {

    fun updateStatus(cafeUuid: String, orderUuid: String, status: OrderStatus)

    fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>>
    fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>>

    fun getAllCafeOrdersByDay(): Flow<List<Statistic>>
    fun getAllCafeOrdersByWeek(): Flow<List<Statistic>>
    fun getAllCafeOrdersByMonth(): Flow<List<Statistic>>
    fun getCafeOrdersByCafeIdAndDay(cafeId: String): Flow<List<Statistic>>
    fun getCafeOrdersByCafeIdAndWeek(cafeId: String): Flow<List<Statistic>>
    fun getCafeOrdersByCafeIdAndMonth(cafeId: String): Flow<List<Statistic>>
}