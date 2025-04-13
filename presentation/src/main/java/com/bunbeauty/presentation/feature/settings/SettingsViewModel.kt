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
    private val updateTypeWorkUseCase: UpdateTypeWorkUseCase, // который обновляет кафе
    private val updateWorkCafeUseCase: UpdateWorkCafeUseCase
) : BaseStateViewModel<SettingsState.DataState, SettingsState.Action, SettingsState.Event>(
    initState = SettingsState.DataState(
        state = SettingsState.DataState.State.LOADING,
        isLoading = false,
        isUnlimitedNotifications = true,
        workType = SettingsState.DataState.WorkType.DELIVERY,
        showAcceptOrdersConfirmation = false,
        workLoad = SettingsState.DataState.WorkLoad.LOW
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
            workType = SettingsState.DataState.WorkType.CLOSED,
            isUnlimitedNotifications = dataState.isUnlimitedNotifications
        )
    }

    private fun handleSaveSettingsClick(dataState: SettingsState.DataState) {
        if (dataState.workType == SettingsState.DataState.WorkType.CLOSED) {
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
                        workType = data.workType.toUiWorkType(),
                        workLoad = data.workload.toUiWorkLoad(),
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

    private fun selectWorkType(workType: SettingsState.DataState.WorkType) {
        setState {
            copy(workType = workType)
        }
    }

    private fun selectWorkLoad(workLoad: SettingsState.DataState.WorkLoad) {
        setState {
            copy(workLoad = workLoad)
        }
    }

    private fun updateSettings(
        workType: SettingsState.DataState.WorkType,
        isUnlimitedNotifications: Boolean
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isLoading = true)
                }
                updateTypeWorkUseCase(
                    workInfoData = workType.toDomainWorkType()
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
        workLoad: SettingsState.DataState.WorkLoad,
        workType: SettingsState.DataState.WorkType
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isLoading = true)
                }
                updateWorkCafeUseCase(
                    workLoad = workLoad.toDomainWorkLoad(),
                    workInfoData = workType.toDomainWorkType()

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
                workType = SettingsState.DataState.WorkType.CLOSED
            )
        }
    }

    private fun SettingsState.DataState.WorkType.toDomainWorkType(): WorkType {
        return when (this) {
            SettingsState.DataState.WorkType.DELIVERY -> WorkType.DELIVERY
            SettingsState.DataState.WorkType.PICKUP -> WorkType.PICKUP
            SettingsState.DataState.WorkType.DELIVERY_AND_PICKUP -> WorkType.DELIVERY_AND_PICKUP
            SettingsState.DataState.WorkType.CLOSED -> WorkType.CLOSED
        }
    }

    private fun SettingsState.DataState.WorkLoad.toDomainWorkLoad(): WorkLoad {
        return when (this) {
            SettingsState.DataState.WorkLoad.LOW -> WorkLoad.LOW
            SettingsState.DataState.WorkLoad.AVERAGE -> WorkLoad.AVERAGE
            SettingsState.DataState.WorkLoad.HIGH -> WorkLoad.HIGH
        }
    }

    private fun WorkType.toUiWorkType(): SettingsState.DataState.WorkType {
        return when (this) {
            WorkType.DELIVERY -> SettingsState.DataState.WorkType.DELIVERY
            WorkType.PICKUP -> SettingsState.DataState.WorkType.PICKUP
            WorkType.DELIVERY_AND_PICKUP -> SettingsState.DataState.WorkType.DELIVERY_AND_PICKUP
            WorkType.CLOSED -> SettingsState.DataState.WorkType.CLOSED
        }
    }

    private fun WorkLoad.toUiWorkLoad(): SettingsState.DataState.WorkLoad {
        return when (this) {
            WorkLoad.LOW -> SettingsState.DataState.WorkLoad.LOW
            WorkLoad.AVERAGE -> SettingsState.DataState.WorkLoad.AVERAGE
            WorkLoad.HIGH -> SettingsState.DataState.WorkLoad.HIGH
        }
    }
}
