package com.bunbeauty.presentation.feature.settings

import com.bunbeauty.domain.feature.settings.model.UserRole

data class SettingsDataState(
    val state: State,
    val user: User?,
    val isUnlimitedNotifications: Boolean?,
    val eventList: List<SettingsEvent>
) {

    data class User(
        val role: UserRole,
        val userName: String,
    )

    enum class State {
        LOADING,
        SUCCESS,
        ERROR,
    }

    operator fun plus(event: SettingsEvent) = copy(eventList = eventList + event)
    operator fun minus(events: List<SettingsEvent>) = copy(eventList = eventList - events.toSet())
}