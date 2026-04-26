package com.bunbeauty.shared.feature.profile

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.feature.profile.IsOrderAvailableUseCase
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.domain.usecase.LogoutUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class ProfileViewModel(
    private val getUsernameUseCase: GetUsernameUseCase,
    private val isOrderAvailableUseCase: IsOrderAvailableUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseStateViewModel<Profile.DataState, Profile.Action, Profile.Event>(
        initState =
            Profile.DataState(
                state = Profile.DataState.State.LOADING,
                user = null,
                acceptOrders = true,
                showAcceptOrdersConfirmation = false,
                logoutLoading = false,
                isShowLogoutBottomSheet = false,
            ),
    ) {
    init {
        handleUpdateData()
    }

    override fun reduce(
        action: Profile.Action,
        dataState: Profile.DataState,
    ) {
        when (action) {
            Profile.Action.UpdateData -> handleUpdateData()
            Profile.Action.StatisticClick -> handleStatisticClick()
            Profile.Action.MenuClick -> handleMenuClick()
            Profile.Action.LogoutClick -> handleLogoutClick()
            Profile.Action.SettingsClick -> handleSettingsClick()
            Profile.Action.LogoutConfirm -> handleLogoutConfirm()
            Profile.Action.LogoutCancel -> handleLogoutCancel()
            Profile.Action.MapClick -> handleMapClick()
        }
    }

    private fun handleUpdateData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        state = Profile.DataState.State.SUCCESS,
                        user =
                            Profile.DataState.User(
                                role = UserRole.MANAGER,
                                userName =
                                    getUsernameUseCase().lowercase().replaceFirstChar { char ->
                                        char.uppercase()
                                    },
                            ),
                        acceptOrders = isOrderAvailableUseCase(),
                    )
                }
            },
            onError = {
                setState {
                    copy(state = Profile.DataState.State.ERROR)
                }
            },
        )
    }

    private fun handleStatisticClick() {
        sendEvent {
            Profile.Event.OpenStatistic
        }
    }

    private fun handleMenuClick() {
        sendEvent {
            Profile.Event.OpenMenu
        }
    }

    private fun handleSettingsClick() {
        sendEvent {
            Profile.Event.OpenSettings
        }
    }

    private fun handleLogoutClick() {
        setState {
            copy(
                isShowLogoutBottomSheet = true,
            )
        }
    }

    private fun handleMapClick() {
        sendEvent {
            Profile.Event.OpenMap
        }
    }

    private fun handleLogoutConfirm() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        logoutLoading = true,
                    )
                }
                logoutUseCase()
                sendEvent {
                    Profile.Event.OpenLogin
                }
            },
            onError = {
                setState {
                    copy(
                        logoutLoading = false,
                    )
                }
            },
        )
    }

    private fun handleLogoutCancel() {
        setState {
            copy(
                isShowLogoutBottomSheet = false,
            )
        }
    }
}
