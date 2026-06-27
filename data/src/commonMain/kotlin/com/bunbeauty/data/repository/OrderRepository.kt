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

class OrderRepository(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val orderUpdatesWebSocket: OrderUpdatesWebSocket,
    private val serverOrderMapper: IServerOrderMapper,
) : OrderRepo {
    private var orderListCache: List<Order>? = null
    private var orderDetailsCache: MutableMap<String, OrderDetails> = mutableMapOf()

    override suspend fun updateStatus(
        token: String,
        orderUuid: String,
        status: OrderStatus,
    ) {
        when (val result = foodDeliveryApi.updateOrderStatus(token, orderUuid, status)) {
            is ApiResult.Success -> {
                orderListCache =
                    orderListCache?.map { order ->
                        if (order.uuid == orderUuid) {
                            order.copy(orderStatus = status)
                        } else {
                            order
                        }
                    }
                orderDetailsCache[orderUuid]?.let { details ->
                    orderDetailsCache[orderUuid] = details.copy(status = status)
                }
            }

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
                            orderListCache = currentList
                            emit(OrderUpdatesStreamEvent.Orders(list = currentList))
                        }
                    }
                }
        }

    override suspend fun getOrderList(
        token: String,
        cafeUuid: String,
    ): List<Order> =
        getOrderListByCafeUuid(
            token = token,
            cafeUuid = cafeUuid,
        )

    private suspend fun getOrderListByCafeUuid(
        token: String,
        cafeUuid: String,
    ): List<Order> =
        when (val result = foodDeliveryApi.getOrderListByCafeUuid(token, cafeUuid)) {
            is ApiResult.Success -> {
                result.data
                    .results
                    .map(serverOrderMapper::mapOrder)
                    .also { orderList ->
                        orderListCache = orderList
                    }
            }

            is ApiResult.Error -> {
                println("$ORDER_TAG: getOrderListByCafeUuid ${result.apiError.message} ${result.apiError.code}")
                orderListCache ?: throw ServerConnectionException()
            }
        }

    override suspend fun loadOrderByUuid(
        token: String,
        orderUuid: String,
    ): OrderDetails? =
        when (val result = foodDeliveryApi.getOrderByUuid(token, orderUuid)) {
            is ApiResult.Success -> {
                serverOrderMapper
                    .mapOrderDetails(result.data)
                    .also { orderDetails ->
                        orderDetailsCache[orderUuid] = orderDetails
                    }
            }

            is ApiResult.Error -> {
                println("$ORDER_TAG: loadOrderByUuid ${result.apiError.message} ${result.apiError.code}")
                orderDetailsCache[orderUuid]
            }
        }

    override fun clearCache() {
        orderListCache = null
        orderDetailsCache.clear()
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
