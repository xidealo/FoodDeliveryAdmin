package com.bunbeauty.presentation.navigation_event

import com.bunbeauty.presentation.model.ListData

sealed class EditMenuProductNavigationEvent: NavigationEvent() {
    data class ToProductCodeList(val listData: ListData): EditMenuProductNavigationEvent()
}
