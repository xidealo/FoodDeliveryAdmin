package com.bunbeauty.presentation.feature.settings

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.settings.GetIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.settings.GetUsernameUseCase
import com.bunbeauty.domain.feature.settings.UpdateIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.settings.model.UserRole
import com.bunbeauty.domain.use_case.LogoutUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStateMapper: SettingsStateMapper,
    private val getIsUnlimitedNotification: GetIsUnlimitedNotificationUseCase,
    private val getUsername: GetUsernameUseCase,
    private val updateIsUnlimitedNotification: UpdateIsUnlimitedNotificationUseCase,
    private val logout: LogoutUseCase,
) : BaseViewModel() {

    private val mutableDataState = MutableStateFlow(
        SettingsDataState(
            state = SettingsDataState.State.LOADING,
            user = null,
            isUnlimitedNotifications = null,
            eventList = emptyList(),
        )
    )
    val uiState = mutableDataState.mapToStateFlow(viewModelScope, settingsStateMapper::map)

    fun updateData() {
        viewModelScope.launchSafe(
            onError = {
                mutableDataState.update { dataState ->
                    dataState.copy(state = SettingsDataState.State.ERROR)
                }
            },
            block = {
                mutableDataState.update { dataState ->
                    dataState.copy(
                        state = SettingsDataState.State.SUCCESS,
                        user = SettingsDataState.User(
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
            dataState + SettingsEvent.OpenLogoutEvent
        }
    }

    fun onLogoutConfirmed(confirmed: Boolean) {
        if (confirmed) {
            viewModelScope.launchSafe(
                onError = {},
                block = {
                    logout()
                    mutableDataState.update { state ->
                        state + SettingsEvent.OpenLoginEvent
                    }
                }
            )
        }
    }

    fun consumeEvents(events: List<SettingsEvent>) {
        mutableDataState.update { state ->
            state - events
        }
    }

}