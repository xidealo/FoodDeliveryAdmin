package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.data.model.server.order.OrderServer
import com.bunbeauty.data.websocket.ApiResultForWebsocket
import com.bunbeauty.data.websocket.OrderUpdatesWebSocket
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.exception.ServerConnectionException
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.feature.order.OrderUpdatesStreamEvent
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderError
import com.bunbeauty.domain.model.order.details.OrderDetails
import common.ApiResult
import common.Constants.ORDER_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OrderRepository(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val orderUpdatesWebSocket: OrderUpdatesWebSocket,
    private val serverOrderMapper: IServerOrderMapper,
) : OrderRepo {
    override suspend fun updateStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus,
    ) {
        when (val result = foodDeliveryApi.updateOrderStatus(token, orderUuid, status)) {
            is ApiResult.Success -> Unit
            is ApiResult.Error -> {
                println("$ORDER_TAG: updateStatus ${result.apiError.message} ${result.apiError.code}")
                throw ServerConnectionException()
            }
        }
    }

    override suspend fun unsubscribeOrderUpdates(message: String) {
        orderUpdatesWebSocket.unsubscribe(message)
    }

    override suspend fun getOrderUpdatesStream(
        token: String,
        cafeUuid: String,
    ): Flow<OrderUpdatesStreamEvent> =
        flow {
            val initialList =
                getOrderListByCafeUuid(
                    token = token,
                    cafeUuid = cafeUuid,
                )
            emit(OrderUpdatesStreamEvent.Orders(list = initialList))

            var currentList = initialList
            orderUpdatesWebSocket
                .getUpdatedOrderFlowByCafeUuid(token, cafeUuid)
                .collect { apiResult ->
                    when (apiResult) {
                        is ApiResultForWebsocket.Loading -> {
                            emit(OrderUpdatesStreamEvent.Loading(isLoading = apiResult.isLoading))
                        }

                        is ApiResultForWebsocket.Error -> {
                            emit(
                                OrderUpdatesStreamEvent.Error(
                                    error = OrderError(apiResult.apiError.message),
                                ),
                            )
                        }

                        is ApiResultForWebsocket.Success<OrderServer> -> {
                            val order = serverOrderMapper.mapOrder(apiResult.data)
                            currentList =
                                updateOrderList(
                                    orderList = currentList,
                                    newOrder = order,
                                )
                            emit(OrderUpdatesStreamEvent.Orders(list = currentList))
                        }
                    }
                }
        }

    private suspend fun getOrderListByCafeUuid(
        token: String,
        cafeUuid: String,
    ): List<Order> =
        when (val result = foodDeliveryApi.getOrderListByCafeUuid(token, cafeUuid)) {
            is ApiResult.Success -> {
                result.data
                    .results
                    .map(serverOrderMapper::mapOrder)
            }

            is ApiResult.Error -> {
                println("$ORDER_TAG: getOrderListByCafeUuid ${result.apiError.message} ${result.apiError.code}")
                throw ServerConnectionException()
            }
        }

    override suspend fun loadOrderByUuid(
        token: String,
        orderUuid: String,
    ): OrderDetails? =
        when (val result = foodDeliveryApi.getOrderByUuid(token, orderUuid)) {
            is ApiResult.Success -> {
                serverOrderMapper.mapOrderDetails(result.data)
            }

            is ApiResult.Error -> {
                println("$ORDER_TAG: loadOrderByUuid ${result.apiError.message} ${result.apiError.code}")
                null
            }
        }

    override fun clearCache() {
        // no-op (stream state is per collector)
    }

    private fun updateOrderList(
        orderList: List<Order>,
        newOrder: Order,
    ): List<Order> {
        val isExisted =
            orderList.any { order ->
                order.uuid == newOrder.uuid
            }
        return if (isExisted) {
            orderList.map { order ->
                if (order.uuid == newOrder.uuid) {
                    newOrder
                } else {
                    order
                }
            }
        } else {
            buildList {
                add(newOrder)
                addAll(orderList)
            }
        }
    }
}
