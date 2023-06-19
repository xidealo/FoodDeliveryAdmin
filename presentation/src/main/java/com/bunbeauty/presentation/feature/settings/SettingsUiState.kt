package com.bunbeauty.presentation.feature.settings

import com.bunbeauty.domain.feature.settings.model.UserRole

data class SettingsUiState(
    val state: State,
    val eventList: List<SettingsEvent>
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