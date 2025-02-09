package com.bunbeauty.presentation.feature.settings

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.profile.GetIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.UpdateIsUnlimitedNotificationUseCase
import com.bunbeauty.domain.feature.profile.model.GetTypeWorkUseCase
import com.bunbeauty.domain.feature.profile.model.UpdateTypeWorkUseCase
import com.bunbeauty.domain.model.settings.WorkInfo
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.settings.state.SettingsState
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getIsUnlimitedNotification: GetIsUnlimitedNotificationUseCase,
    private val updateIsUnlimitedNotification: UpdateIsUnlimitedNotificationUseCase,
    private val getTypeWorkUseCase: GetTypeWorkUseCase,
    private val updateTypeWorkUseCase: UpdateTypeWorkUseCase
) : BaseStateViewModel<SettingsState.DataState, SettingsState.Action, SettingsState.Event>(
    initState = SettingsState.DataState(
        state = SettingsState.DataState.State.LOADING,
        isLoading = false,
        isUnlimitedNotifications = true,
        workType = SettingsState.DataState.WorkType.DELIVERY,
        showAcceptOrdersConfirmation = false
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

            SettingsState.Action.CancelAcceptOrders -> closeAcceptionDialog()

            SettingsState.Action.ConfirmNotAcceptOrders -> handleConfirmNotAcceptOrders(dataState)

        }
    }

    private fun handleConfirmNotAcceptOrders(dataState: SettingsState.DataState) {
        handleConfirmNotAcceptOrders()
        updateSettings(
            workType = SettingsState.DataState.WorkType.CLOSED,
            isUnlimitedNotifications = dataState.isUnlimitedNotifications
        )
    }

    private fun handleSaveSettingsClick(dataState: SettingsState.DataState) {
        if (dataState.workType == SettingsState.DataState.WorkType.CLOSED) {
            showAcceptionDialog()
        } else {
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

    private fun showAcceptionDialog() {
        setState {
            copy(showAcceptOrdersConfirmation = true)
        }
    }

    private fun closeAcceptionDialog() {
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
        viewModelScope.launchSafe(
            block = {
                loadTypeWork()
                loadNotifications()
            },
            onError = {
                showErrorState()
            },
        )
    }

    private fun selectWorkType(workType: SettingsState.DataState.WorkType) {
        setState {
            copy(workType = workType)
        }
    }

    private fun updateSettings(
        workType: SettingsState.DataState.WorkType,
        isUnlimitedNotifications: Boolean
    ) {
        viewModelScope.launchSafe(
            block = {
                setState { copy(isLoading = true) }
                updateTypeWorkUseCase(
                    workInfoData = mapWorkTypeToWorkInfoData(workType = workType)
                )
                updateIsUnlimitedNotification(
                    isEnabled = isUnlimitedNotifications
                )
                sendEvent {
                    SettingsState.Event.ShowSaveSettingEvent
                }
            },
            onError = {
                SettingsState.Event.ShowErrorMessage(messageId = R.string.error_settings_not_save)
            }
        )
    }

    private fun loadNotifications() {
        viewModelScope.launchSafe(
            block = {
                val stateNotification = getIsUnlimitedNotification()
                setState {
                    copy(isUnlimitedNotifications = stateNotification)
                }
            },
            onError = {
                showErrorState()
            },
        )
    }

    private fun loadTypeWork() {
        viewModelScope.launchSafe(
            block = {
                val typeWork = getTypeWorkUseCase()
                updateLoadTypeWork(typeWork)
                setState {
                    copy(
                        state = SettingsState.DataState.State.SUCCESS,
                        workType = typeWork.workType.toSettingsWorkType()
                    )
                }
            },
            onError = {
                showErrorState()
            }
        )
    }

    private fun updateLoadTypeWork(workInfoData: WorkInfo?) {
        if (workInfoData == null) {
            showErrorState()
            return
        }

        setState {
            copy(
                state = SettingsState.DataState.State.SUCCESS,
                workType = workType
            )
        }
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
                workType = SettingsState.DataState.WorkType.CLOSED
            )
        }
    }

    private fun WorkInfo.WorkType.toSettingsWorkType(): SettingsState.DataState.WorkType {
        return when (this) {
            WorkInfo.WorkType.DELIVERY -> SettingsState.DataState.WorkType.DELIVERY
            WorkInfo.WorkType.PICKUP -> SettingsState.DataState.WorkType.PICKUP
            WorkInfo.WorkType.DELIVERY_AND_PICKUP -> SettingsState.DataState.WorkType.DELIVERY_AND_PICKUP
            WorkInfo.WorkType.CLOSED -> SettingsState.DataState.WorkType.CLOSED
        }
    }

    private fun mapWorkTypeToWorkInfoData(workType: SettingsState.DataState.WorkType): WorkInfo {
        return WorkInfo(
            workType = WorkInfo.WorkType.valueOf(workType.toString())
        )
    }
}
