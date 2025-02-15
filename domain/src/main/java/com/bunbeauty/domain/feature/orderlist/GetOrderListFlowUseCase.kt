package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class GetOrderListFlowUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo
) {

    suspend operator fun invoke(cafeUuid: String): Flow<List<Order>> {
        val token = dataStoreRepo.getToken() ?: return emptyFlow()

        return orderRepo.getOrderListFlow(
            token = token,
            cafeUuid = cafeUuid
        ).map { orderList ->
            orderList.filter { order ->
                order.orderStatus != OrderStatus.CANCELED
            }
        }
    }
}
