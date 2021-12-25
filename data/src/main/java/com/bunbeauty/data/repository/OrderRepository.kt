package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import kotlinx.coroutines.flow.*
import okhttp3.internal.notify
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val dateTimeUtil: IDateTimeUtil,
    private val serverOrderMapper: IServerOrderMapper,
) : OrderRepo {

    override val ordersMapFlow: MutableSharedFlow<List<Order>> = MutableSharedFlow()
    private var cachedData: MutableMap<String, Order> = HashMap()

    override suspend fun updateStatus(cafeUuid: String, orderUuid: String, status: OrderStatus) {
        networkConnector.updateOrderStatus(cafeUuid, orderUuid, status)
    }

    override suspend fun subscribeOnOrderListByCafeId(token: String, cafeId: String) {
        networkConnector.subscribeOnOrderListByCafeId(token, cafeId).filter {
            it is ApiResult.Success
        }.map { resultApiResultSuccess ->
            serverOrderMapper.toModel((resultApiResultSuccess as ApiResult.Success).data)
                .let { order ->
                    cachedData[order.uuid] = order
                    ordersMapFlow.emit(cachedData.values.sortedByDescending { it.time })
                }
        }.collect()
    }

    override suspend fun loadOrderListByCafeId(
        token: String,
        cafeId: String
    ) {
        when (val result = networkConnector.getOrderListByCafeId(token, cafeId)) {
            is ApiResult.Success -> {
                if(result.data.results.isEmpty()){
                    ordersMapFlow.emit(emptyList())
                }else{
                    cachedData =
                        result.data.results.map(serverOrderMapper::toModel).map { it.uuid to it }
                            .toMap() as MutableMap<String, Order>

                    ordersMapFlow.emit(cachedData.values.sortedByDescending { it.time })
                }
            }
            is ApiResult.Error -> {
                //ApiResult.Error(result.apiError)
            }
        }
    }
}