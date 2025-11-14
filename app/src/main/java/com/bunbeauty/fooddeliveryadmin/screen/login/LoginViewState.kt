package com.bunbeauty.fooddeliveryadmin.screen.login

data class LoginViewState(
    val isLoading: Boolean = true,
    val eventList: List<Event> = emptyList(),
) {
    sealed interface Event {
        data object OpenOrderListEvent : Event

        data object ShowWrongCredentialError : Event

        data object ShowConnectionError : Event
    }

    operator fun plus(event: Event) = copy(eventList = eventList + event)

    operator fun minus(events: List<Event>) = copy(eventList = eventList - events.toSet())
}
