package com.bunbeauty.presentation.feature.statistic

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface Statistic {
    data class DataState(
        val selectedTimeInterval: TimeIntervalCode = TimeIntervalCode.MONTH,
        val statisticList: List<StatisticItemModel> = emptyList(),
        val state: State,
        val isTimeIntervalListShown: Boolean = false,
        val loadingStatistic: Boolean,
        val cafeAddress: String? = null,
        val cafeUuid: String? = null
    ) : BaseDataState {

        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }

        data class StatisticItemModel(
            val startMillis: Long,
            val period: String,
            val count: Int,
            val proceeds: String,
            val date: String
        )
    }

    sealed interface Action : BaseAction {

        data object Init : Action

        data object LoadStatisticClick : Action
        data object SelectTimeIntervalClick : Action
        data object SelectGoBackClick : Action
        data object CloseTimeIntervalListBottomSheet : Action
        data class SelectedTimeInterval(val timeInterval: TimeIntervalCode) : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event
    }

    data class SelectedCafe(
        val uuid: String?,
        val address: String?
    )
}

enum class TimeIntervalCode {
    DAY,
    WEEK,
    MONTH
}
