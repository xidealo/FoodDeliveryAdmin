package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.ORDER_TAG
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val serverOrderMapper: IServerOrderMapper,
) {

    private val mutableOrderListFlow: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())
    val orderListFlow: StateFlow<List<Order>> = mutableOrderListFlow.asStateFlow()

    suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus) {
        networkConnector.updateOrderStatus(token, orderUuid, status)
    }

    suspend fun subscribeOnOrderList(token: String, cafeUuid: String) {
        networkConnector.subscribeOnOrderListByCafeId(token, cafeUuid).map { result ->
            if (result is ApiResult.Success) {
                val order = serverOrderMapper.toModel(result.data)
                updateOrderList(order)
            }
        }.launchIn(CoroutineScope(IO))
    }

    suspend fun unsubscribeOnOrderList(message: String) {
        networkConnector.unsubscribeOnOrderList(message)
    }

    suspend fun loadOrderListByCafeUuid(token: String, cafeUuid: String) {
        when (val result = networkConnector.getOrderListByCafeId(token, cafeUuid)) {
            is ApiResult.Success -> {
                mutableOrderListFlow.update {
                    result.data.results.map(serverOrderMapper::toModel)
                        .filter { order ->
                            order.orderStatus != OrderStatus.CANCELED
                        }.sortedByDescending { order ->
                            order.time
                        }
                }
            }
            is ApiResult.Error -> {
                Log.e(
                    ORDER_TAG,
                    "loadOrderListByCafeUuid ${result.apiError.message} ${result.apiError.code}"
                )
            }
        }
    }

    suspend fun loadOrderByUuid(token: String, orderUuid: String): OrderDetails? {
        return when (val result = networkConnector.getOrderByUuid(token, orderUuid)) {
            is ApiResult.Success -> {
                serverOrderMapper.toModel(result.data)
            }
            is ApiResult.Error -> {
                Log.e(
                    ORDER_TAG,
                    "loadOrderByUuid ${result.apiError.message} ${result.apiError.code}"
                )
                null
            }
        }
    }

    private fun updateOrderList(newOrder: Order) {
        mutableOrderListFlow.update { orderList ->
            val isExisted = orderList.any { order ->
                order.uuid == newOrder.uuid
            }
            if (isExisted) {
                orderList.map { order ->
                    if (order.uuid == newOrder.uuid) {
                        newOrder
                    } else {
                        order
                    }
                }
            } else {
                (orderList + newOrder).sortedByDescending { it.time }
            }
        }
    }
}