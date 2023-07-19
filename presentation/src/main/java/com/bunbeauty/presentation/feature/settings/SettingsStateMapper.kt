package com.bunbeauty.presentation.feature.settings

import javax.inject.Inject

class SettingsStateMapper @Inject constructor() {

    fun map(dataState: SettingsDataState): SettingsUiState {
        return SettingsUiState(
            state = when (dataState.state) {
                SettingsDataState.State.LOADING -> SettingsUiState.State.Loading
                SettingsDataState.State.ERROR -> SettingsUiState.State.Error
                SettingsDataState.State.SUCCESS -> {
                    if (dataState.user == null || dataState.isUnlimitedNotifications == null) {
                        SettingsUiState.State.Error
                    } else {
                        SettingsUiState.State.Success(
                            role = dataState.user.role,
                            userName = dataState.user.userName,
                            isUnlimitedNotifications = dataState.isUnlimitedNotifications,
                        )
                    }
                }
            },
            eventList = dataState.eventList,
        )
    }

}