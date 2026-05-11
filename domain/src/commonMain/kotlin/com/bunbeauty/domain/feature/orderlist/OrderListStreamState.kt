package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderError

sealed interface OrderListStreamState {
    data class Loading(
        val isLoading: Boolean,
    ) : OrderListStreamState

    data class Orders(
        val list: List<Order>,
    ) : OrderListStreamState

    data class Error(
        val error: OrderError,
    ) : OrderListStreamState
}
