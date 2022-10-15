package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.OrderRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val serverOrderMapper: IServerOrderMapper,
) : OrderRepo {

    override val orderListFlow: MutableSharedFlow<List<Order>> = MutableSharedFlow()

    private val cachedData: MutableMap<String, Order> = mutableMapOf()

    override suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus) {
        networkConnector.updateOrderStatus(token, orderUuid, status)
    }

    override suspend fun subscribeOnOrderList(token: String, cafeId: String) {
        networkConnector.subscribeOnOrderListByCafeId(token, cafeId).filter {
            it is ApiResult.Success
        }.map { resultApiResultSuccess ->
            serverOrderMapper.toModel((resultApiResultSuccess as ApiResult.Success).data)
                .let { order ->
                    cachedData[order.uuid] = order
                    orderListFlow.emit(cachedData.values.sortedByDescending { it.time })
                }
        }.collect()
    }

    override suspend fun unsubscribeOnOrderList(cafeId: String, message: String) {
        networkConnector.unsubscribeOnOrderList(cafeId, message)
    }

    override suspend fun loadOrderListByCafeId(
        token: String,
        cafeId: String
    ) {
        cachedData.clear()
        when (val result = networkConnector.getOrderListByCafeId(token, cafeId)) {
            is ApiResult.Success -> {
                if (result.data.results.isEmpty()) {
                    orderListFlow.emit(emptyList())
                } else {
                    cachedData.clear()
                    cachedData.putAll(
                        result.data.results.map(serverOrderMapper::toModel).associateBy { it.uuid }
                    )
                    orderListFlow.emit(cachedData.values.sortedByDescending { it.time })
                }
            }
            is ApiResult.Error -> {
                Log.e("testTag", "loadOrderListByCafeId ${result.apiError.message} ${result.apiError.code}")
            }
        }
    }

    override suspend fun subscribeOnNotification(cafeId: String) {
        networkConnector.subscribeOnNotification(cafeId)
    }

    override suspend fun unsubscribeOnNotification(cafeId: String) {
        networkConnector.unsubscribeOnNotification(cafeId)
    }
}