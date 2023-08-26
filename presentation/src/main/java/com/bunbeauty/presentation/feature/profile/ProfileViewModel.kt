package com.bunbeauty.presentation.feature.profile

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.profile.GetIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.feature.profile.UpdateIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.domain.usecase.LogoutUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileStateMapper: ProfileStateMapper,
    private val getIsUnlimitedNotification: GetIsUnlimitedNotificationUseCase,
    private val getUsername: GetUsernameUseCase,
    private val updateIsUnlimitedNotification: UpdateIsUnlimitedNotificationUseCase,
    private val logout: LogoutUseCase,
) : BaseViewModel() {

    private val mutableDataState = MutableStateFlow(
        ProfileDataState(
            state = ProfileDataState.State.LOADING,
            user = null,
            isUnlimitedNotifications = null,
            eventList = emptyList(),
        )
    )
    val uiState = mutableDataState.mapToStateFlow(viewModelScope, profileStateMapper::map)

    fun updateData() {
        viewModelScope.launchSafe(
            onError = {
                mutableDataState.update { dataState ->
                    dataState.copy(state = ProfileDataState.State.ERROR)
                }
            },
            block = {
                mutableDataState.update { dataState ->
                    dataState.copy(
                        state = ProfileDataState.State.SUCCESS,
                        user = ProfileDataState.User(
                            role = UserRole.MANAGER,
                            userName = getUsername().lowercase().replaceFirstChar { char ->
                                char.titlecase()
                            },
                        ),
                        isUnlimitedNotifications = getIsUnlimitedNotification(),
                    )
                }
            }
        )
    }

    fun onUnlimitedNotificationsCheckChanged(isChecked: Boolean) {
        viewModelScope.launchSafe(
            onError = {},
            block = {
                updateIsUnlimitedNotification(isChecked)
                mutableDataState.update { dataState ->
                    dataState.copy(isUnlimitedNotifications = isChecked)
                }
            }
        )
    }

    fun onLogoutClick() {
        mutableDataState.update { dataState ->
            dataState + ProfileEvent.OpenLogoutEvent
        }
    }

    fun onLogoutConfirmed(confirmed: Boolean) {
        if (confirmed) {
            viewModelScope.launchSafe(
                onError = {},
                block = {
                    logout()
                    mutableDataState.update { state ->
                        state + ProfileEvent.OpenLoginEvent
                    }
                }
            )
        }
    }

    fun consumeEvents(events: List<ProfileEvent>) {
        mutableDataState.update { state ->
            state - events
        }
    }

}