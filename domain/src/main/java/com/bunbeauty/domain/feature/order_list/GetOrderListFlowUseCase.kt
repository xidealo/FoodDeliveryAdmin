package com.bunbeauty.domain.feature.order_list

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOrderListFlowUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo
) {

    suspend operator fun invoke(cafeUuid: String): Flow<List<Order>> {
        val token = dataStoreRepo.token.firstOrNull() ?: return emptyFlow()

        return orderRepo.getOrderListFlow(
            token = token,
            cafeUuid = cafeUuid,
        ).map { orderList ->
            orderList.filter {  order ->
                order.orderStatus != OrderStatus.CANCELED
            }
        }
    }

}