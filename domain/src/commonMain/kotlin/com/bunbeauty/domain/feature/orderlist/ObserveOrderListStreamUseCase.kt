package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.feature.order.OrderUpdatesStreamEvent
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class ObserveOrderListStreamUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo,
) {
    suspend operator fun invoke(cafeUuid: String): Flow<OrderListStreamState> {
        val token = dataStoreRepo.getToken() ?: return emptyFlow()

        return orderRepo
            .getOrderUpdatesStream(
                token = token,
                cafeUuid = cafeUuid,
            ).map { event ->
                when (event) {
                    is OrderUpdatesStreamEvent.Loading -> {
                        OrderListStreamState.Loading(isLoading = event.isLoading)
                    }

                    is OrderUpdatesStreamEvent.Error -> {
                        OrderListStreamState.Error(error = event.error)
                    }

                    is OrderUpdatesStreamEvent.Orders -> {
                        OrderListStreamState.Orders(
                            list =
                                event.list.filter { order ->
                                    order.orderStatus != OrderStatus.CANCELED
                                },
                        )
                    }
                }
            }
    }
}
