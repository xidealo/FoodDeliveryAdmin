package com.bunbeauty.presentation.navigation_event

sealed class LoginNavigationEvent: NavigationEvent() {
    object ToOrders: LoginNavigationEvent()
}