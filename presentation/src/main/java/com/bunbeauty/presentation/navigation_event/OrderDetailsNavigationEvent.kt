package com.bunbeauty.presentation.navigation_event

import com.bunbeauty.presentation.model.ListData

sealed class OrderDetailsNavigationEvent: NavigationEvent() {

    data class ToStatusList(val listData: ListData): OrderDetailsNavigationEvent()
}
