package com.bunbeauty.presentation.feature.profile

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.profile.GetUsernameUseCase
import com.bunbeauty.domain.feature.profile.IsOrderAvailableUseCase
import com.bunbeauty.domain.feature.profile.UpdateOrderAvailabilityUseCase
import com.bunbeauty.domain.feature.profile.model.UserRole
import com.bunbeauty.domain.usecase.LogoutUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class ProfileViewModel (
    private val getUsernameUseCase: GetUsernameUseCase,
    private val isOrderAvailableUseCase: IsOrderAvailableUseCase,
    private val updateOrderAvailabilityUseCase: UpdateOrderAvailabilityUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseStateViewModel<Profile.DataState, Profile.Action, Profile.Event>(
    initState = Profile.DataState(
        state = Profile.DataState.State.LOADING,
        user = null,
        acceptOrders = true,
        showAcceptOrdersConfirmation = false,
        logoutLoading = false
    )
) {

    override fun reduce(action: Profile.Action, dataState: Profile.DataState) {
        when (action) {
            Profile.Action.UpdateData -> handleUpdateData()
            Profile.Action.CafeClick -> handleCafeClick()
            Profile.Action.SettingsClick -> handleSettingsClick()
            Profile.Action.StatisticClick -> handleStatisticClick()
            Profile.Action.AcceptOrdersClick -> handleAcceptOrdersClick()
            Profile.Action.ConfirmAcceptOrders -> handleConfirmAcceptOrders()
            Profile.Action.CancelAcceptOrders -> handleCancelAcceptOrders()
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
                        acceptOrders = isOrderAvailableUseCase()
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

    private fun handleAcceptOrdersClick() {
        setState {
            copy(
                acceptOrders = !acceptOrders,
                showAcceptOrdersConfirmation = true
            )
        }
    }

    private fun handleConfirmAcceptOrders() {
        setState {
            copy(showAcceptOrdersConfirmation = false)
        }
        viewModelScope.launchSafe(
            block = {
                val updatedValue = updateOrderAvailabilityUseCase(
                    isAvailable = mutableDataState.value.acceptOrders
                )
                setState {
                    copy(acceptOrders = updatedValue)
                }
            },
            onError = {
                setState {
                    copy(acceptOrders = !acceptOrders)
                }
            }
        )
    }

    private fun handleCancelAcceptOrders() {
        setState {
            copy(
                acceptOrders = !acceptOrders,
                showAcceptOrdersConfirmation = false
            )
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
                block = {
                    setState {
                        copy(
                            logoutLoading = true
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
                            logoutLoading = false
                        )
                    }
                }
            )
        }
    }
}
