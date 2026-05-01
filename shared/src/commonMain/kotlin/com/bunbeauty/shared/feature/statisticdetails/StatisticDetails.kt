package com.bunbeauty.shared.feature.statisticdetails

import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseEvent
import com.bunbeauty.shared.viewmodel.base.BaseViewDataState

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
