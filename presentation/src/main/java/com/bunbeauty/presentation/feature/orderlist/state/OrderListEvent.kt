package com.bunbeauty.presentation.feature.orderlist.state

import com.bunbeauty.presentation.feature.cafelist.SelectableCafeItem

sealed interface OrderListEvent {
    object ScrollToTop : OrderListEvent
    class OpenCafeListEvent(val cafeList: List<SelectableCafeItem>) : OrderListEvent
    class OpenOrderDetailsEvent(val orderUuid: String, val orderCode: String) : OrderListEvent
    object OpenLoginEvent : OrderListEvent
    object ShowError : OrderListEvent
    object CancelNotification : OrderListEvent
}