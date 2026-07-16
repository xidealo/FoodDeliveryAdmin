package com.bunbeauty.shared.feature.statisticuserdetails

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.clientuser.GetClientUserStatisticUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class StatisticUserDetailsViewModel(
    private val getClientUserStatisticUseCase: GetClientUserStatisticUseCase,
) : BaseStateViewModel<StatisticUserDetails.DataState, StatisticUserDetails.Action, StatisticUserDetails.Event>(
        initState =
            StatisticUserDetails.DataState(
                state = StatisticUserDetails.DataState.State.LOADING,
                statistic = null,
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

    private fun handleBackClick() {
        sendEvent {
            StatisticUserDetails.Event.GoBack
        }
    }
}
