package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val serverOrderMapper: IServerOrderMapper,
) {

    private val mutableOrderListFlow: MutableSharedFlow<List<Order>> = MutableSharedFlow()
    val orderListFlow: SharedFlow<List<Order>> = mutableOrderListFlow.asSharedFlow()

    private val cachedData: MutableMap<String, Order> = mutableMapOf()

    suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus) {
        networkConnector.updateOrderStatus(token, orderUuid, status)
    }

    suspend fun subscribeOnOrderList(token: String, cafeId: String) {
        networkConnector.subscribeOnOrderListByCafeId(token, cafeId).filter {
            it is ApiResult.Success
        }.map { resultApiResultSuccess ->
            serverOrderMapper.toModel((resultApiResultSuccess as ApiResult.Success).data)
                .let { order ->
                    cachedData[order.uuid] = order
                    mutableOrderListFlow.emit(cachedData.values.sortedByDescending { it.time })
                }
        }.collect()
    }

    suspend fun unsubscribeOnOrderList(cafeId: String, message: String) {
        networkConnector.unsubscribeOnOrderList(cafeId, message)
    }

    suspend fun loadOrderListByCafeUuid(token: String, cafeUuid: String) {
        cachedData.clear()
        when (val result = networkConnector.getOrderListByCafeId(token, cafeUuid)) {
            is ApiResult.Success -> {
                if (result.data.results.isEmpty()) {
                    mutableOrderListFlow.emit(emptyList())
                } else {
                    cachedData.clear()
                    cachedData.putAll(
                        result.data.results.map(serverOrderMapper::toModel).associateBy { it.uuid }
                    )
                    mutableOrderListFlow.emit(cachedData.values.sortedByDescending { it.time })
                }
            }
            is ApiResult.Error -> {
                Log.e("order", "loadOrderListByCafeUuid ${result.apiError.message} ${result.apiError.code}")
            }
        }
    }

    suspend fun loadOrderByUuid(token: String, orderUuid: String): Order? {
        return when (val result = networkConnector.getOrderByUuid(token, orderUuid)) {
            is ApiResult.Success -> {
                serverOrderMapper.toModel(result.data)
            }
            is ApiResult.Error -> {
                Log.e("order", "loadOrderByUuid ${result.apiError.message} ${result.apiError.code}")
                null
            }
        }
    }

    suspend fun subscribeOnNotification(cafeId: String) {
        networkConnector.subscribeOnNotification(cafeId)
    }

    suspend fun unsubscribeOnNotification(cafeId: String) {
        networkConnector.unsubscribeOnNotification(cafeId)
    }
}