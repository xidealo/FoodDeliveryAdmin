package com.bunbeauty.domain.repository.order

import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderEntity
import com.bunbeauty.domain.date_time.IDateTimeUtil
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val apiRepository: IApiRepository,
    private val dateTimeUtil: IDateTimeUtil,
) : OrderRepo {

    override suspend fun insert(orderEntity: OrderEntity) = orderDao.insert(orderEntity)

    override fun update(order: Order) {
        apiRepository.updateOrder(order)
    }

    override fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return apiRepository.getAddedOrderListByCafeId(cafeId).map { orderList ->
            orderList.filter { it.orderEntity.orderStatus != OrderStatus.CANCELED }
        }
    }

    override fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>> {
        return apiRepository.getUpdatedOrderListByCafeId(cafeId).map { orderList ->
            orderList.filter { it.orderEntity.orderStatus != OrderStatus.CANCELED }
        }
    }

    override fun getAllCafeOrdersByDay(): Flow<Map<String, List<Order>>> {
        return mapToStatisticList(apiRepository.getAllOrderList(), dateTimeUtil::getDateDDMMMMYYYY)
    }

    override fun getAllCafeOrdersByWeek(): Flow<Map<String, List<Order>>> {
        return mapToStatisticList(apiRepository.getAllOrderList(), dateTimeUtil::getWeekPeriod)
    }

    override fun getAllCafeOrdersByMonth(): Flow<Map<String, List<Order>>> {
        return mapToStatisticList(apiRepository.getAllOrderList(), dateTimeUtil::getDateMMMMYYYY)
    }

    override fun getCafeOrdersByCafeIdAndDay(cafeId: String): Flow<Map<String, List<Order>>> {
        return mapToStatisticList(
            apiRepository.getOrderListByCafeId(cafeId),
            dateTimeUtil::getDateDDMMMMYYYY
        )
    }

    override fun getCafeOrdersByCafeIdAndWeek(cafeId: String): Flow<Map<String, List<Order>>> {
        return mapToStatisticList(
            apiRepository.getOrderListByCafeId(cafeId),
            dateTimeUtil::getWeekPeriod
        )
    }

    override fun getCafeOrdersByCafeIdAndMonth(cafeId: String): Flow<Map<String, List<Order>>> {
        return mapToStatisticList(
            apiRepository.getOrderListByCafeId(cafeId),
            dateTimeUtil::getDateMMMMYYYY
        )
    }

    fun mapToStatisticList(
        orderListFlow: Flow<List<Order>>,
        timestampConverter: (Long) -> String
    ): Flow<Map<String, List<Order>>> {
        return orderListFlow.map { orderList ->
            orderList.reversed()
                .groupBy { order ->
                    timestampConverter.invoke(order.timestamp)
                }
        }
    }
}