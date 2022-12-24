package com.bunbeauty.fooddeliveryadmin.screen.login

data class LoginViewState(
    val isLoading: Boolean = true,
    val appVersion: String? = null,
    val eventList: List<Event> = emptyList()
) {
    sealed interface Event {
        object OpenOrderListEvent : Event
        object ShowWrongCredentialError : Event
        object ShowConnectionError : Event
    }

    operator fun plus(event: Event) = copy(eventList = eventList + event)
    operator fun minus(events: List<Event>) = copy(eventList = eventList - events.toSet())
}