package com.bunbeauty.presentation.feature.orderlist.state

import com.bunbeauty.presentation.feature.selectcafe.SelectableCafeItem

sealed interface OrderListEvent {
    data object ScrollToTop : OrderListEvent
    class OpenCafeListEvent(val cafeList: List<SelectableCafeItem>) : OrderListEvent
    class OpenOrderDetailsEvent(val orderUuid: String, val orderCode: String) : OrderListEvent
    data object OpenLoginEvent : OrderListEvent
    data object ShowError : OrderListEvent
    data object CancelNotification : OrderListEvent
}