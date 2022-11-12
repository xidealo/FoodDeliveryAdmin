package com.bunbeauty.fooddeliveryadmin.screen.login

data class LoginViewState(
    val isLoading: Boolean = true,
    val appVersion: String? = null,
    val events: List<Event> = emptyList()
) {
    sealed interface Event {
        object OpenOrderListEvent : Event
        object ShowLoginError : Event
    }
}