package com.bunbeauty.presentation.feature.profile

import com.bunbeauty.domain.feature.profile.model.UserRole

data class ProfileDataState(
    val state: State,
    val user: User?,
    val isUnlimitedNotifications: Boolean?,
    val eventList: List<ProfileEvent>
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

    operator fun plus(event: ProfileEvent) = copy(eventList = eventList + event)
    operator fun minus(events: List<ProfileEvent>) = copy(eventList = eventList - events.toSet())
}