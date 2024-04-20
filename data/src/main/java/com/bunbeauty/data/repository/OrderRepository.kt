package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.ORDER_TAG
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.data.model.server.order.OrderServer
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.exception.ServerConnectionException
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderError
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.repo.OrderRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val serverOrderMapper: IServerOrderMapper
) : OrderRepo {

    private var cachedOrderList: List<Order> = emptyList()

    override suspend fun updateStatus(token: String, orderUuid: String, status: OrderStatus) {
        networkConnector.updateOrderStatus(token, orderUuid, status)
    }

    override suspend fun getOrderListFlow(token: String, cafeUuid: String): Flow<List<Order>> {
        val orderList = getOrderListByCafeUuid(
            token = token,
            cafeUuid = cafeUuid
        )
        cachedOrderList = orderList
        val updatedOrderListFlow = networkConnector.getUpdatedOrderFlowByCafeUuid(token, cafeUuid)
            .filterIsInstance<ApiResult.Success<OrderServer>>()
            .map { successApiResult ->
                val order = serverOrderMapper.mapOrder(successApiResult.data)
                val updatedOrderList = updateOrderList(cachedOrderList, order)
                cachedOrderList = updatedOrderList
                updatedOrderList
            }.onCompletion {
                networkConnector.unsubscribeOnOrderList("onCompletion")
            }

        return merge(
            flowOf(orderList),
            updatedOrderListFlow
        )
    }

    override suspend fun getOrderErrorFlow(token: String, cafeUuid: String): Flow<OrderError> {
        return networkConnector.getUpdatedOrderFlowByCafeUuid(token, cafeUuid)
            .filterIsInstance<ApiResult.Error<OrderServer>>()
            .map { errorApiResult ->
                OrderError(errorApiResult.apiError.message)
            }
    }

    private suspend fun getOrderListByCafeUuid(token: String, cafeUuid: String): List<Order> {
        return when (val result = networkConnector.getOrderListByCafeUuid(token, cafeUuid)) {
            is ApiResult.Success -> {
                result.data
                    .results
                    .map(serverOrderMapper::mapOrder)
            }

            is ApiResult.Error -> {
                Log.e(
                    ORDER_TAG,
                    "getOrderListByCafeUuid ${result.apiError.message} ${result.apiError.code}"
                )
                throw ServerConnectionException()
            }
        }
    }

    override suspend fun loadOrderByUuid(token: String, orderUuid: String): OrderDetails? {
        return when (val result = networkConnector.getOrderByUuid(token, orderUuid)) {
            is ApiResult.Success -> {
                serverOrderMapper.mapOrderDetails(result.data)
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

    private fun updateOrderList(orderList: List<Order>, newOrder: Order): List<Order> {
        val isExisted = orderList.any { order ->
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
