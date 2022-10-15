package com.bunbeauty.fooddeliveryadmin.screen.order_list

import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option

data class OrderListState(
    val selectedCafe: SelectedCafe? = null,
    val orderList: List<OrderItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val eventList: List<Event> = emptyList()
) {

    sealed class Event {
        class OpenCafeListEvent(val cafeList: List<Option>) : Event()
    }
}

data class SelectedCafe(
    val uuid: String,
    val address: String
)