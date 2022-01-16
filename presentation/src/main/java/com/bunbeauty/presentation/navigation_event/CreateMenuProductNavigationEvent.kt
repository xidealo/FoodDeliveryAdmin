package com.bunbeauty.presentation.navigation_event

import com.bunbeauty.presentation.model.ListData

sealed class CreateMenuProductNavigationEvent: NavigationEvent() {
    data class ToProductCodeList(val listData: ListData): CreateMenuProductNavigationEvent()
}