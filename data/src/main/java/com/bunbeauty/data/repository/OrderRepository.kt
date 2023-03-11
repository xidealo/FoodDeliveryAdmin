package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.ORDER_TAG
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderDetails
import com.bunbeauty.domain.model.order.OrderListResult
import com.bunbeauty.domain.repo.OrderRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val serverOrderMapper: IServerOrderMapper,
) : OrderRepo {

    var observeOrderJob: Job? = null

    private var orderList: List<Order> = emptyList()
    private val mutableOrderListFlow: MutableSharedFlow<OrderListResult> = MutableSharedFlow()
    override val orderListFlow: Flow<OrderListResult> =
        mutableOrderListFlow.map { orderListResult ->
            if (orderListResult is OrderListResult.Success) {
                OrderListResult.Success(
                    orderList = orderListResult.orderList.filter { order ->
                        order.orderStatus != OrderStatus.CANCELED
                    }
                )
            } else {
                orderListResult
            }
        }

    override suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus) {
        networkConnector.updateOrderStatus(token, orderUuid, status)
    }

    override suspend fun subscribeOnOrderList(token: String, cafeUuid: String) {
        observeOrderJob =
            networkConnector.subscribeOnOrderListByCafeId(token, cafeUuid).map { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val order = serverOrderMapper.toModel(result.data)
                        updateOrderList(order)
                    }
                    is ApiResult.Error -> {
                        Log.e(
                            ORDER_TAG,
                            "subscribeOnOrderList ${result.apiError.message} ${result.apiError.code}"
                        )
                        mutableOrderListFlow.emit(OrderListResult.Error)
                    }
                }
            }.launchIn(CoroutineScope(IO))
    }

    override suspend fun unsubscribeOnOrderList(message: String) {
        observeOrderJob?.cancelAndJoin()
        observeOrderJob = null
        networkConnector.unsubscribeOnOrderList(message)
    }

    suspend fun loadOrderListByCafeUuid(token: String, cafeUuid: String) {
        when (val result = networkConnector.getOrderListByCafeId(token, cafeUuid)) {
            is ApiResult.Success -> {
                val processedOrderList = result.data.results.map(serverOrderMapper::toModel)
                    .sortedByDescending { order ->
                        order.time
                    }
                orderList = processedOrderList
                mutableOrderListFlow.emit(OrderListResult.Success(processedOrderList))
            }
            is ApiResult.Error -> {
                Log.e(
                    ORDER_TAG,
                    "loadOrderListByCafeUuid ${result.apiError.message} ${result.apiError.code}"
                )
                mutableOrderListFlow.emit(OrderListResult.Error)
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

    private suspend fun updateOrderList(newOrder: Order) {
        val isExisted = orderList.any { order ->
            order.uuid == newOrder.uuid
        }
        val updatedOrderList = if (isExisted) {
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
        orderList = updatedOrderList
        mutableOrderListFlow.emit(OrderListResult.Success(updatedOrderList))
    }
}