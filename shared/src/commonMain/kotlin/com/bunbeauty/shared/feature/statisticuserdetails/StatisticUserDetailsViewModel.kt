package com.bunbeauty.shared.feature.statisticuserdetails

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.clientuser.GetClientUserStatisticUseCase
import com.bunbeauty.domain.feature.clientuser.UpdateClientUserProblematicUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class StatisticUserDetailsViewModel(
    private val getClientUserStatisticUseCase: GetClientUserStatisticUseCase,
    private val updateClientUserProblematicUseCase: UpdateClientUserProblematicUseCase,
) : BaseStateViewModel<StatisticUserDetails.DataState, StatisticUserDetails.Action, StatisticUserDetails.Event>(
        initState =
            StatisticUserDetails.DataState(
                state = StatisticUserDetails.DataState.State.LOADING,
                statistic = null,
                isProblematic = false,
                initialIsProblematic = false,
                saving = false,
            ),
    ) {
    private var userUuid: String? = null

    override fun reduce(
        action: StatisticUserDetails.Action,
        dataState: StatisticUserDetails.DataState,
    ) {
        when (action) {
            is StatisticUserDetails.Action.Init -> handleInit(action.userUuid)
            StatisticUserDetails.Action.Retry -> loadStatistic()
            StatisticUserDetails.Action.BackClick -> handleBackClick()
            is StatisticUserDetails.Action.OnProblematicChecked -> {
                setState {
                    copy(isProblematic = action.isProblematic)
                }
            }
            StatisticUserDetails.Action.OnSaveClick -> handleSave()
            StatisticUserDetails.Action.OnDiscountClick -> handleDiscountClick(dataState)
            is StatisticUserDetails.Action.OnPersonalDiscountUpdated ->
                handlePersonalDiscountUpdated(action.personalDiscountPercent)
        }
    }

    private fun handlePersonalDiscountUpdated(personalDiscountPercent: Int?) {
        val normalizedPersonalDiscountPercent = personalDiscountPercent?.takeIf { it > 0 }
        setState {
            copy(
                statistic =
                    statistic?.copy(
                        personalDiscountPercent = normalizedPersonalDiscountPercent,
                    ),
            )
        }
    }

    private fun handleDiscountClick(dataState: StatisticUserDetails.DataState) {
        val phoneNumber = dataState.statistic?.phoneNumber ?: return
        sendEvent {
            StatisticUserDetails.Event.OpenDiscount(phoneNumber = phoneNumber)
        }
    }

    private fun handleInit(userUuid: String) {
        if (this.userUuid == userUuid && state.value.state == StatisticUserDetails.DataState.State.SUCCESS) {
            return
        }
        this.userUuid = userUuid
        loadStatistic()
    }

    private fun loadStatistic() {
        val currentUserUuid = userUuid ?: return
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(state = StatisticUserDetails.DataState.State.LOADING)
                }
                val statistic = getClientUserStatisticUseCase(clientUserUuid = currentUserUuid)
                setState {
                    copy(
                        state = StatisticUserDetails.DataState.State.SUCCESS,
                        statistic = statistic,
                        isProblematic = statistic.isProblematic,
                        initialIsProblematic = statistic.isProblematic,
                        saving = false,
                    )
                }
            },
            onError = {
                setState {
                    copy(state = StatisticUserDetails.DataState.State.ERROR)
                }
            },
        )
    }

    private fun handleSave() {
        val currentUserUuid = userUuid ?: return
        val currentState = state.value
        if (!currentState.hasChanges || currentState.saving) {
            return
        }
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(saving = true)
                }
                val updated =
                    updateClientUserProblematicUseCase(
                        clientUserUuid = currentUserUuid,
                        isProblematic = currentState.isProblematic,
                    )
                setState {
                    copy(
                        saving = false,
                        isProblematic = updated.isProblematic,
                        initialIsProblematic = updated.isProblematic,
                        statistic = statistic?.copy(isProblematic = updated.isProblematic),
                    )
                }
                sendEvent {
                    StatisticUserDetails.Event.ShowSavedMessage
                }
            },
            onError = {
                setState {
                    copy(saving = false)
                }
            },
        )
    }

    private fun handleBackClick() {
        sendEvent {
            StatisticUserDetails.Event.GoBack
        }
    }
}
