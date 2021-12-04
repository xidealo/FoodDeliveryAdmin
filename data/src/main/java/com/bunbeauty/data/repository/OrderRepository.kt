package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.data.model.server.order.ServerOrder
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val dateTimeUtil: IDateTimeUtil,
    private val serverOrderMapper: IServerOrderMapper,
) : OrderRepo {

    override suspend fun updateStatus(cafeUuid: String, orderUuid: String, status: OrderStatus) {
        networkConnector.updateOrderStatus(cafeUuid, orderUuid, status)
    }

    override suspend fun getOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return networkConnector.getOrderListByCafeId(cafeId).filter {
            it is ApiResult.Success
        }.map { resultApiResultSuccess ->
            (resultApiResultSuccess as ApiResult.Success).data?.results?.map(serverOrderMapper::toModel)
                ?: emptyList()
        }
    }
}