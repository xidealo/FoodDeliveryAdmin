package com.bunbeauty.data.repository

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderEntity
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.data.dao.OrderDao
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val apiRepo: ApiRepo,
    private val dateTimeUtil: IDateTimeUtil,
) : OrderRepo {

    override suspend fun insert(orderEntity: OrderEntity) = orderDao.insert(orderEntity)

    override fun updateStatus(cafeId: String, orderUuid: String, status: OrderStatus) {
        apiRepo.updateOrderStatus(cafeId, orderUuid, status)
    }

    override fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return apiRepo.getAddedOrderListByCafeId(cafeId).map { orderList ->
            orderList.filter { it.orderEntity.orderStatus != OrderStatus.CANCELED }
        }
    }

    override fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return apiRepo.getUpdatedOrderListByCafeId(cafeId).map { orderList ->
            orderList.filter { it.orderEntity.orderStatus != OrderStatus.CANCELED }
        }
    }

    override fun getAllCafeOrdersByDay(): Flow<List<Statistic>> {
        return mapToStatisticList(apiRepo.getAllOrderList(), dateTimeUtil::getDateDDMMMMYYYY)
    }

    override fun getAllCafeOrdersByWeek(): Flow<List<Statistic>> {
        return mapToStatisticList(apiRepo.getAllOrderList(), dateTimeUtil::getWeekPeriod)
    }

    override fun getAllCafeOrdersByMonth(): Flow<List<Statistic>> {
        return mapToStatisticList(apiRepo.getAllOrderList(), dateTimeUtil::getDateMMMMYYYY)
    }

    override fun getCafeOrdersByCafeIdAndDay(cafeId: String): Flow<List<Statistic>> {
        return mapToStatisticList(
            apiRepo.getOrderListByCafeId(cafeId),
            dateTimeUtil::getDateDDMMMMYYYY
        )
    }

    override fun getCafeOrdersByCafeIdAndWeek(cafeId: String): Flow<List<Statistic>> {
        return mapToStatisticList(
            apiRepo.getOrderListByCafeId(cafeId),
            dateTimeUtil::getWeekPeriod
        )
    }

    override fun getCafeOrdersByCafeIdAndMonth(cafeId: String): Flow<List<Statistic>> {
        return mapToStatisticList(
            apiRepo.getOrderListByCafeId(cafeId),
            dateTimeUtil::getDateMMMMYYYY
        )
    }

    fun mapToStatisticList(
        orderListFlow: Flow<List<Order>>,
        timestampConverter: (Long) -> String
    ): Flow<List<Statistic>> {
        return orderListFlow.flowOn(IO)
            .map { orderList ->
                val orderMap = orderList.filter { order ->
                    order.orderEntity.orderStatus != OrderStatus.CANCELED
                }.groupBy { order ->
                    timestampConverter.invoke(order.timestamp)
                }
                orderMap.map { orderEntry ->
                    Statistic(
                        period = orderEntry.key,
                        orderList = orderEntry.value
                    )
                }.sortedByDescending { statistic ->
                    statistic.orderList.first().timestamp
                }
            }.flowOn(Default)
    }
}