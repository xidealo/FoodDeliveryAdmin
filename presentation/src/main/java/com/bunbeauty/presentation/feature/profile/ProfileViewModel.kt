package com.bunbeauty.presentation.feature.profile

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.domain.usecase.LogoutUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUsernameUseCase: GetUsernameUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseStateViewModel<Profile.DataState, Profile.Action, Profile.Event>(
    initState = Profile.DataState(
        state = Profile.DataState.State.LOADING,
        user = null,
        acceptOrders = null,
    )
) {

    override fun reduce(action: Profile.Action, dataState: Profile.DataState) {
        when (action) {
            Profile.Action.UpdateData -> handleUpdateData()
            Profile.Action.CafeClick -> handleCafeClick()
            Profile.Action.SettingsClick -> handleSettingsClick()
            Profile.Action.StatisticClick -> handleStatisticClick()
            is Profile.Action.AcceptOrdersToggle -> handleAcceptOrdersToggle(checked = action.checked)
            Profile.Action.LogoutClick -> handleLogoutClick()
            is Profile.Action.LogoutConfirm -> handleLogoutConfirm(confirmed = action.confirmed)
        }
    }

    private fun handleUpdateData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        state = Profile.DataState.State.SUCCESS,
                        user = Profile.DataState.User(
                            role = UserRole.MANAGER,
                            userName = getUsernameUseCase().lowercase().replaceFirstChar { char ->
                                char.uppercase()
                            }
                        ),
                        acceptOrders = true
                    )
                }
            },
            onError = {
                setState {
                    copy(state = Profile.DataState.State.ERROR)
                }
            }
        )
    }

    private fun handleCafeClick() {
        sendEvent {
            Profile.Event.OpenCafeList
        }
    }

    private fun handleSettingsClick() {
        sendEvent {
            Profile.Event.OpenSettings
        }
    }

    private fun handleStatisticClick() {
        sendEvent {
            Profile.Event.OpenStatistic
        }
    }

    private fun handleAcceptOrdersToggle(checked: Boolean) {
        // TODO sent to server
        setState {
            copy(acceptOrders = checked)
        }
    }

    private fun handleLogoutClick() {
        sendEvent {
            Profile.Event.OpenLogout
        }
    }

    private fun handleLogoutConfirm(confirmed: Boolean) {
        if (confirmed) {
            viewModelScope.launchSafe(
                onError = {
                    // Not handled
                },
                block = {
                    logoutUseCase()
                    sendEvent {
                        Profile.Event.OpenLogin
                    }
                }
            )
        }
    }

}
