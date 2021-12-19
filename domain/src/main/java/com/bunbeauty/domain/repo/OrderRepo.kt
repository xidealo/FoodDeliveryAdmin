package com.bunbeauty.domain.repo

import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.statistic.Statistic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface OrderRepo {

    val ordersMapFlow: MutableSharedFlow<MutableMap<String, Order>>

    suspend fun updateStatus(cafeUuid: String, orderUuid: String, status: OrderStatus)

    suspend fun subscribeOnOrderListByCafeId(token: String, cafeId: String)
    suspend fun loadOrderListByCafeId(token: String, cafeId: String)
}