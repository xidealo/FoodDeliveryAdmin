package com.bunbeauty.domain.feature.order.usecase

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class UpdateOrderStatusUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo
) {

    suspend operator fun invoke(orderUuid: String, status: OrderStatus) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        orderRepo.updateStatus(
            token = token,
            orderUuid = orderUuid,
            status = status
        )
    }
}
