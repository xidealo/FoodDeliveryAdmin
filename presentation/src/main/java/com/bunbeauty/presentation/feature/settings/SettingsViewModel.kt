package com.bunbeauty.presentation.feature.settings

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.profile.GetIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.UpdateIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.model.GetTypeWorkUseCase
import com.bunbeauty.domain.feature.profile.model.UpdateTypeWorkUseCase
import com.bunbeauty.domain.feature.profile.model.UpdateWorkCafeUseCase
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.settings.state.SettingsState
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class SettingsViewModel(
    private val getIsUnlimitedNotification: GetIsUnlimitedNotificationUseCase,
    private val updateIsUnlimitedNotification: UpdateIsUnlimitedNotificationUseCase,
    private val getTypeWorkUseCase: GetTypeWorkUseCase,
    private val updateTypeWorkUseCase: UpdateTypeWorkUseCase,
    private val updateWorkCafeUseCase: UpdateWorkCafeUseCase
) : BaseStateViewModel<SettingsState.DataState, SettingsState.Action, SettingsState.Event>(
    initState = SettingsState.DataState(
        state = SettingsState.DataState.State.LOADING,
        isLoading = false,
        isUnlimitedNotifications = true,
        workType = WorkType.DELIVERY,
        showAcceptOrdersConfirmation = false,
        workLoad = WorkLoad.LOW
    )
) {
    override fun reduce(
        action: SettingsState.Action,
        dataState: SettingsState.DataState
    ) {
        when (action) {
            is SettingsState.Action.Init -> loadData()
            SettingsState.Action.OnBackClicked -> onBackClicked()
            is SettingsState.Action.OnNotificationsClicked -> setNotificationStatus(action = action)

            is SettingsState.Action.OnSaveSettingsClick -> handleSaveSettingsClick(
                dataState = dataState
            )

            is SettingsState.Action.OnSelectStatusClicked -> selectWorkType(workType = action.workType)

            SettingsState.Action.CancelAcceptOrders -> closeAcceptDialog()

            SettingsState.Action.ConfirmNotAcceptOrders -> handleConfirmNotAcceptOrders(dataState)
            is SettingsState.Action.OnSelectWorkLoadClicked -> selectWorkLoad(workLoad = action.workload)
        }
    }

    private fun handleConfirmNotAcceptOrders(dataState: SettingsState.DataState) {
        handleConfirmNotAcceptOrders()
        updateSettings(
            workType = WorkType.CLOSED,
            isUnlimitedNotifications = dataState.isUnlimitedNotifications
        )
    }

    private fun handleSaveSettingsClick(dataState: SettingsState.DataState) {
        if (dataState.workType == WorkType.CLOSED) {
            showAcceptDialog()
        } else {
            updateCafe(
                workLoad = dataState.workLoad,
                workType = dataState.workType
            )
            updateSettings(
                workType = dataState.workType,
                isUnlimitedNotifications = dataState.isUnlimitedNotifications
            )
        }
    }

    private fun setNotificationStatus(action: SettingsState.Action.OnNotificationsClicked) {
        setState {
            copy(isUnlimitedNotifications = action.isUnlimitedNotifications)
        }
    }

    private fun showAcceptDialog() {
        setState {
            copy(showAcceptOrdersConfirmation = true)
        }
    }

    private fun closeAcceptDialog() {
        setState {
            copy(showAcceptOrdersConfirmation = false)
        }
    }

    private fun onBackClicked() {
        sendEvent {
            SettingsState.Event.GoBackEvent
        }
    }

    private fun loadData() {
        setState {
            copy(
                state = SettingsState.DataState.State.LOADING
            )
        }
        viewModelScope.launchSafe(
            block = {
                val data = getTypeWorkUseCase()
                setState {
                    copy(
                        workType = data.workType,
                        workLoad = data.workload,
                        isUnlimitedNotifications = getIsUnlimitedNotification(),
                        state = SettingsState.DataState.State.SUCCESS,
                        isLoading = false
                    )
                }
            },
            onError = {
                showErrorState()
            }
        )
    }

    private fun selectWorkType(workType: WorkType) {
        setState {
            copy(workType = workType)
        }
    }

    private fun selectWorkLoad(workLoad: WorkLoad) {
        setState {
            copy(workLoad = workLoad)
        }
    }

    private fun updateSettings(
        workType: WorkType,
        isUnlimitedNotifications: Boolean
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isLoading = true)
                }
                updateTypeWorkUseCase(
                    workInfoData = workType
                )
                updateIsUnlimitedNotification(
                    isEnabled = isUnlimitedNotifications
                )
                sendEvent {
                    SettingsState.Event.ShowSaveSettingEvent
                }
            },
            onError = {
                showErrorState()
            }
        )
    }

    private fun updateCafe(
        workLoad: WorkLoad,
        workType: WorkType
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isLoading = true)
                }
                updateWorkCafeUseCase(
                    workLoad = workLoad,
                    workInfoData = workType

                )
                sendEvent {
                    SettingsState.Event.ShowSaveSettingEvent
                }
            },
            onError = {
                showErrorState()
            }
        )
    }

    private fun showErrorState() {
        setState {
            copy(state = SettingsState.DataState.State.ERROR)
        }
    }

    private fun handleConfirmNotAcceptOrders() {
        setState {
            copy(
                showAcceptOrdersConfirmation = false,
                workType = WorkType.CLOSED
            )
        }
    }
}
