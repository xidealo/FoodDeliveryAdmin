package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.server.ServerOrder
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val apiRepo: ApiRepo,
    private val dateTimeUtil: IDateTimeUtil,
    private val serverOrderMapper: IServerOrderMapper,
) : OrderRepo {

    override suspend fun updateStatus(cafeUuid: String, orderUuid: String, status: OrderStatus) {
        apiRepo.updateOrderStatus(cafeUuid, orderUuid, status)
    }

    override fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return apiRepo.getAddedOrderListByCafeId(cafeId)
            .flowOn(IO)
            .map { serverOrderList ->
                serverOrderList.map { serverOrder ->
                    serverOrderMapper.from(serverOrder)
                }.filter { order ->
                    order.orderStatus != CANCELED
                }.sortedByDescending { order ->
                    order.time
                }
            }.flowOn(Default)
    }

    override fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return apiRepo.getUpdatedOrderListByCafeId(cafeId)
            .flowOn(IO)
            .map { serverOrderList ->
                serverOrderList.map { serverOrder ->
                    serverOrderMapper.from(serverOrder)
                }.filter { order ->
                    order.orderStatus != CANCELED
                }.sortedByDescending { order ->
                    order.time
                }
            }.flowOn(Default)
    }

    override fun getAllCafeOrdersByDay(): Flow<List<Statistic>> {
        return mapToStatisticList(
            mapToOrderListFlow(apiRepo.getAllOrderList()),
            dateTimeUtil::getDateDDMMMMYYYY
        )
    }

    override fun getAllCafeOrdersByWeek(): Flow<List<Statistic>> {
        return mapToStatisticList(
            mapToOrderListFlow(apiRepo.getAllOrderList()),
            dateTimeUtil::getWeekPeriod
        )
    }

    override fun getAllCafeOrdersByMonth(): Flow<List<Statistic>> {
        return mapToStatisticList(
            mapToOrderListFlow(apiRepo.getAllOrderList()),
            dateTimeUtil::getDateMMMMYYYY
        )
    }

    override fun getCafeOrdersByCafeIdAndDay(cafeId: String): Flow<List<Statistic>> {
        return mapToStatisticList(
            mapToOrderListFlow(apiRepo.getOrderListByCafeId(cafeId)),
            dateTimeUtil::getDateDDMMMMYYYY
        )
    }

    override fun getCafeOrdersByCafeIdAndWeek(cafeId: String): Flow<List<Statistic>> {
        return mapToStatisticList(
            mapToOrderListFlow(apiRepo.getOrderListByCafeId(cafeId)),
            dateTimeUtil::getWeekPeriod
        )
    }

    override fun getCafeOrdersByCafeIdAndMonth(cafeId: String): Flow<List<Statistic>> {
        return mapToStatisticList(
            mapToOrderListFlow(apiRepo.getOrderListByCafeId(cafeId)),
            dateTimeUtil::getDateMMMMYYYY
        )
    }

    fun mapToOrderListFlow(serverOrderListFlow: Flow<List<ServerOrder>>): Flow<List<Order>> {
        return serverOrderListFlow.map { serverOrderList ->
            serverOrderList.map { serverOrder ->
                serverOrderMapper.from(serverOrder)
            }
        }
    }

    fun mapToStatisticList(
        orderListFlow: Flow<List<Order>>,
        timestampConverter: (Long) -> String
    ): Flow<List<Statistic>> {
        return orderListFlow.flowOn(IO)
            .map { orderList ->
                val orderMap = orderList.filter { order ->
                    order.orderStatus == DELIVERED || order.orderStatus == DONE
                }.groupBy { order ->
                    timestampConverter.invoke(order.time)
                }
                orderMap.map { orderEntry ->
                    Statistic(
                        period = orderEntry.key,
                        orderList = orderEntry.value
                    )
                }.sortedByDescending { statistic ->
                    statistic.orderList.first().time
                }
            }.flowOn(Default)
    }
}