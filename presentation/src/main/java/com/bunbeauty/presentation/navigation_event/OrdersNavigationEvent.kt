package com.bunbeauty.presentation.navigation_event

import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.presentation.model.ListData

sealed class OrdersNavigationEvent: NavigationEvent() {
    data class ToCafeAddressList(val listData: ListData): OrdersNavigationEvent()
    data class ToOrderDetails(val order: Order): OrdersNavigationEvent()
}