package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.model.order.OrderError
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetOrderErrorFlowUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepository: OrderRepo
) {

    suspend operator fun invoke(cafeUuid: String): Flow<OrderError> {
        val token = dataStoreRepo.getToken() ?: return emptyFlow()
        return orderRepository.getOrderErrorFlow(
            token = token,
            cafeUuid = cafeUuid
        )
    }
}
