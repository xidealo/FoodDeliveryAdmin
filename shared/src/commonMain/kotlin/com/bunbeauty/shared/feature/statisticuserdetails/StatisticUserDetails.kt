package com.bunbeauty.shared.feature.statisticuserdetails

import com.bunbeauty.domain.feature.clientuser.model.ClientUserStatistic
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface StatisticUserDetails {
    data class DataState(
        val state: State,
        val statistic: ClientUserStatistic?,
        val isProblematic: Boolean,
        val initialIsProblematic: Boolean,
        val saving: Boolean,
    ) : BaseDataState {
        val hasChanges: Boolean
            get() = isProblematic != initialIsProblematic

        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val userUuid: String,
        ) : Action

        data object Retry : Action

        data object BackClick : Action

        data class OnProblematicChecked(
            val isProblematic: Boolean,
        ) : Action

        data object OnSaveClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event

        data object ShowSavedMessage : Event
    }
}
