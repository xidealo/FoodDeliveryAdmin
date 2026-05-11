package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.feature.order.OrderRepo

class UnsubscribeOrderUpdatesUseCase(
    private val orderRepo: OrderRepo,
) {
    suspend operator fun invoke(message: String) {
        orderRepo.unsubscribeOrderUpdates(message = message)
    }
}
