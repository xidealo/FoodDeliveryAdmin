package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.model.order.OrderError
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetOrderErrorFlowUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val orderRepository: OrderRepo
) {

    suspend operator fun invoke(): Flow<OrderError> {
        val token = dataStoreRepo.token.firstOrNull() ?: return emptyFlow()
        val cafeUuid = getSelectedCafe()?.uuid ?: return emptyFlow()

        return orderRepository.getOrderErrorFlow(
            token = token,
            cafeUuid = cafeUuid
        )
    }
}
