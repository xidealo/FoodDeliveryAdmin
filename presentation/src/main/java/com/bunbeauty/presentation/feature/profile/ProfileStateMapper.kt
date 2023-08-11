package com.bunbeauty.presentation.feature.profile

import javax.inject.Inject

class ProfileStateMapper @Inject constructor() {

    fun map(dataState: ProfileDataState): ProfileUiState {
        return ProfileUiState(
            state = when (dataState.state) {
                ProfileDataState.State.LOADING -> ProfileUiState.State.Loading
                ProfileDataState.State.ERROR -> ProfileUiState.State.Error
                ProfileDataState.State.SUCCESS -> {
                    if (dataState.user == null) {
                        ProfileUiState.State.Error
                    } else {
                        ProfileUiState.State.Success(
                            role = dataState.user.role,
                            userName = dataState.user.userName,
                        )
                    }
                }
            },
            eventList = dataState.eventList,
        )
    }

}