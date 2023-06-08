package com.bunbeauty.presentation.view_model.main

import androidx.navigation.NavController

data class MainUiState(
    val connectionLost: Boolean = false,
    val navigationBarOptions: NavigationBarOptions = NavigationBarOptions.Hidden,
    val eventList: List<Event> = emptyList(),
) {

    sealed interface Event {
        class ShowMessageEvent(val message: AdminMessage) : Event
    }

    operator fun plus(event: Event) = copy(eventList = eventList + event)
    operator fun minus(events: List<Event>) = copy(eventList = eventList - events.toSet())
}

enum class AdminMessageType {
    INFO,
    ERROR,
}

data class AdminMessage(
    val type: AdminMessageType,
    val text: String
)

enum class AdminNavigationBarItem {
    ORDERS,
    STATISTIC,
    MENU,
}

sealed interface NavigationBarOptions {
    object Hidden : NavigationBarOptions
    data class Visible(
        val selectedItem: AdminNavigationBarItem,
        val navController: NavController
    ) : NavigationBarOptions
}