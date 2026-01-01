package com.bunbeauty.presentation.feature.statisticdetails

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState


interface StatisticDetails {
    data class DataState(
        val state: State,
    ) : BaseViewDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data object Init : Action

    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event
    }
}