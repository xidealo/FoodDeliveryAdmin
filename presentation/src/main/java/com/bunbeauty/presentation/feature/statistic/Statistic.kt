package com.bunbeauty.presentation.feature.statistic

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface Statistic {
    data class ViewDataState(
        val cafeUuid: String?,
        val selectedCafe: SelectedCafe? = null,
        val selectedTimeInterval: TimeIntervalCode = TimeIntervalCode.MONTH,
        val statisticList: List<StatisticItemModel> = emptyList(),
        val eventList: List<Event> = emptyList(),
        val isLoading: Boolean = false
    ) : BaseViewDataState {

        data class StatisticItemModel(
            val startMillis: Long,
            val period: String,
            val count: String,
            val proceeds: String,
            val date: String
        )
    }

    sealed interface Action : BaseAction {

        data object Init : Action

        data object LoadStatisticClick : Action
        data object SelectCafeClick : Action
    }

    sealed interface Event : BaseEvent {

        data object GoBack : Event
        class OpenCafeListEvent(val cafeList: List<Cafe>) : Event
        class OpenTimeIntervalListEvent(val timeIntervalList: List<Option>) : Event
        class ShowError(val retryAction: RetryAction) : Event
    }

    enum class RetryAction {
        LOAD_CAFE_LIST,
        LOAD_STATISTIC
    }

    data class SelectedCafe(
        val uuid: String?,
        val address: String?
    )

    enum class TimeIntervalCode {
        DAY,
        WEEK,
        MONTH
    }
}