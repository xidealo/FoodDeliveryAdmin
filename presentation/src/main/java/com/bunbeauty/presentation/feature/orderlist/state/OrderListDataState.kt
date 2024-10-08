package com.bunbeauty.presentation.feature.orderlist.state

import com.bunbeauty.domain.model.cafe.SelectedCafe
import com.bunbeauty.domain.model.order.Order

internal data class OrderListDataState(
    val refreshing: Boolean,
    val selectedCafe: SelectedCafe?,
    val cafeState: State,
    val orderList: List<Order>?,
    val orderListState: State,
    val eventList: List<OrderListEvent>
) {

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }

    operator fun plus(event: OrderListEvent) = copy(eventList = eventList + event)
    operator fun minus(events: List<OrderListEvent>) = copy(eventList = eventList - events.toSet())
}
