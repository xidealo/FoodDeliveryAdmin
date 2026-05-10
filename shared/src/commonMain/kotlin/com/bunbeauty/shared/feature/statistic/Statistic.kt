package com.bunbeauty.shared.feature.statistic

import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface Statistic {
    data class DataState(
        val selectedTimeInterval: TimeIntervalCode = TimeIntervalCode.MONTH,
        val statisticList: List<StatisticItemModel> = emptyList(),
        val state: State,
        val isTimeIntervalListShown: Boolean = false,
        val loadingStatistic: Boolean,
        val cafeAddress: String? = null,
        val cafeUuid: String? = null,
        val cafeOffsetHours: Int? = null,
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }

        data class StatisticItemModel(
            val startMillis: Long,
            val period: String,
            val count: Int,
            val proceeds: String,
            val date: String,
        )
    }

    sealed interface Action : BaseAction {
        data object Init : Action

        data object LoadStatisticClick : Action

        data object SelectTimeIntervalClick : Action

        data object SelectGoBackClick : Action

        data object CloseTimeIntervalListBottomSheet : Action

        data class SelectedTimeInterval(
            val timeInterval: TimeIntervalCode,
        ) : Action

        data class DayRowClick(
            val startMillis: Long,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event

        data class NavigateToDayDetail(
            val dateIso: String,
        ) : Event
    }
}

enum class TimeIntervalCode {
    DAY,
    WEEK,
    MONTH,
}
