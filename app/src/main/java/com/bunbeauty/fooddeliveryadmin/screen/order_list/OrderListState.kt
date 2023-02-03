package com.bunbeauty.fooddeliveryadmin.screen.order_list

import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.SelectedCafe
import com.bunbeauty.fooddeliveryadmin.screen.order_list.list.OrderItemModel

data class OrderListState(
    val selectedCafe: SelectedCafe? = null,
    val orderList: List<OrderItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val eventList: List<Event> = emptyList()
) {

    sealed interface Event {
        object ScrollToTop : Event
        class OpenCafeListEvent(val cafeList: List<Option>) : Event
        class OpenOrderDetailsEvent(val orderUuid: String) : Event
        object OpenLoginEvent : Event
        object ShowError : Event
    }

    operator fun plus(event: Event) = copy(eventList = eventList + event)
    operator fun minus(events: List<Event>) = copy(eventList = eventList - events.toSet())
}
