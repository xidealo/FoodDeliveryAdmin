package com.bunbeauty.fooddeliveryadmin.screen.order_list

import com.bunbeauty.presentation.Option

sealed interface OrderListEvent {
    object ScrollToTop : OrderListEvent
    class OpenCafeListEvent(val cafeList: List<Option>) : OrderListEvent
    class OpenOrderDetailsEvent(val orderUuid: String, val orderCode: String) : OrderListEvent
    object OpenLoginEvent : OrderListEvent
    object ShowError : OrderListEvent
    object CancelNotification : OrderListEvent
}