package com.bunbeauty.domain.feature.order

import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderError

sealed interface OrderUpdatesStreamEvent {
    data class Loading(
        val isLoading: Boolean,
    ) : OrderUpdatesStreamEvent

    data class Orders(
        val list: List<Order>,
    ) : OrderUpdatesStreamEvent

    data class Error(
        val error: OrderError,
    ) : OrderUpdatesStreamEvent
}
