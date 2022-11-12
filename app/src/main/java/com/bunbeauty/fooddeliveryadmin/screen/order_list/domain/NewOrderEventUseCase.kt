package com.bunbeauty.fooddeliveryadmin.screen.order_list.domain

import com.bunbeauty.data.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class NewOrderEventUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    operator fun invoke(): Flow<Unit> {
        var previousOrderSize = 0
        return orderRepository.orderListFlow.map { orderList ->
            orderList.size
        }.filter { currentOrderSize ->
            currentOrderSize > previousOrderSize
        }.onEach { currentOrderSize ->
            previousOrderSize = currentOrderSize
        }.map {}
    }
}