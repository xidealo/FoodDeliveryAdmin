package com.bunbeauty.presentation.feature.statistic

import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface Statistic {
    data class ViewDataState(
        val cafeUuid: String?,
        val selectedCafe: SelectedCafe? = null,
        val selectedTimeInterval: SelectedTimeInterval? = null,
        val statisticList: List<StatisticItemModel> = emptyList(),
        val eventList: List<Event> = emptyList(),
        val isLoading: Boolean = false
    ) : BaseViewDataState {

        val selectedTimeIntervalCode: String
            get() = (selectedTimeInterval?.code ?: TimeIntervalCode.MONTH).toString()

        data class StatisticItemModel(
            val startMillis: Long,
            val period: String,
            val count: String,
            val proceeds: String
        )
    }

    sealed interface Action : BaseAction {

        data object Init : Action

        data object LoadStatisticClick : Action
        data object SelectCafeClick : Action
    }

    sealed interface Event : BaseEvent {

        data object GoBack : Event
        class OpenCafeListEvent(val cafeList: List<Option>) : Event
        class OpenTimeIntervalListEvent(val timeIntervalList: List<Option>) : Event
        class ShowError(val retryAction: RetryAction) : Event
    }

    enum class RetryAction {
        LOAD_CAFE_LIST,
        LOAD_STATISTIC
    }

    data class SelectedCafe(
        val uuid: String?,
        val address: String
    )

    data class SelectedTimeInterval(
        val code: TimeIntervalCode,
        val name: String
    )

    enum class TimeIntervalCode {
        DAY,
        WEEK,
        MONTH
    }
}