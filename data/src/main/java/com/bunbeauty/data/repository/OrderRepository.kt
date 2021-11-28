package com.bunbeauty.data.repository

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val dateTimeUtil: IDateTimeUtil,
    private val serverOrderMapper: IServerOrderMapper,
) : OrderRepo {

    override suspend fun updateStatus(cafeUuid: String, orderUuid: String, status: OrderStatus) {
        networkConnector.updateOrderStatus(cafeUuid, orderUuid, status)
    }

    override fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return flow{

        }
       /* networkConnector.getAddedOrderListByCafeId(cafeId)
            .flowOn(IO)
            .map { serverOrderList ->
                serverOrderList.map { serverOrder ->
                    serverOrderMapper.from(serverOrder)
                }.filter { order ->
                    order.orderStatus != CANCELED
                }.sortedByDescending { order ->
                    order.time
                }
            }.flowOn(Default)*/
    }

    override fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return flow{

        }
    /*    networkConnector.getUpdatedOrderListByCafeId(cafeId)
            .flowOn(IO)
            .map { serverOrderList ->
                serverOrderList.map { serverOrder ->
                    serverOrderMapper.from(serverOrder)
                }.filter { order ->
                    order.orderStatus != CANCELED
                }.sortedByDescending { order ->
                    order.time
                }
            }.flowOn(Default)*/
    }

    override fun getAllCafeOrdersByDay(): Flow<List<Statistic>> {
        return  flow{

        }

      /*  mapToStatisticList(
            mapToOrderListFlow(networkConnector.orderList),
            dateTimeUtil::getDateDDMMMMYYYY
        )*/
    }

    override fun getAllCafeOrdersByWeek(): Flow<List<Statistic>> {
        return flow {

        }
     /*
        mapToStatisticList(
            mapToOrderListFlow(networkConnector.orderList),
            dateTimeUtil::getWeekPeriod
        )*/
    }

    override fun getAllCafeOrdersByMonth(): Flow<List<Statistic>> {
        return flow {

        }
        /*mapToStatisticList(
            mapToOrderListFlow(networkConnector.orderList),
            dateTimeUtil::getDateMMMMYYYY
        )*/
    }

    override fun getCafeOrdersByCafeIdAndDay(cafeId: String): Flow<List<Statistic>> {
        return flow{

        } /* mapToStatisticList(
            mapToOrderListFlow(networkConnector.getOrderListByCafeId(cafeId)),
            dateTimeUtil::getDateDDMMMMYYYY
        )*/
    }

    override fun getCafeOrdersByCafeIdAndWeek(cafeId: String): Flow<List<Statistic>> {
        return  flow{

        } /*mapToStatisticList(
            mapToOrderListFlow(networkConnector.getOrderListByCafeId(cafeId)),
            dateTimeUtil::getWeekPeriod
        )*/
    }

    override fun getCafeOrdersByCafeIdAndMonth(cafeId: String): Flow<List<Statistic>> {
        return  flow{

        } /*mapToStatisticList(
            mapToOrderListFlow(networkConnector.getOrderListByCafeId(cafeId)),
            dateTimeUtil::getDateMMMMYYYY
        )*/
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