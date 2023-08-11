package com.bunbeauty.presentation.feature.profile

import com.bunbeauty.domain.feature.profile.model.UserRole

data class ProfileUiState(
    val state: State,
    val eventList: List<ProfileEvent>
) {

    val isLogoutEnabled: Boolean
        get() = state != State.Loading

    sealed interface State {
        object Loading : State
        object Error : State
        data class Success(
            val role: UserRole,
            val userName: String,
            val isUnlimitedNotifications: Boolean,
        ) : State
    }

}