package com.bunbeauty.presentation.feature.statistic

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface Statistic {
    data class DataState(
        val cafeUuid: String?,
        val selectedCafe: SelectedCafe? = null,
        val selectedTimeInterval: TimeIntervalCode = TimeIntervalCode.MONTH,
        val statisticList: List<StatisticItemModel> = emptyList(),
        val isLoading: Boolean = true,
        val hasError: Boolean = false,
        val isTimeIntervalListShown: Boolean = false,
        val isCafeListShown: Boolean = false,
        val cafeList: List<Cafe> = emptyList()
    ) : BaseDataState {

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
        data object SelectCafeClick : Action
        data object SelectTimeIntervalClick : Action
        data object SelectGoBackClick : Action
        data object CloseTimeIntervalListBottomSheet : Action
        data object CloseCafeListBottomSheet : Action
        data class SelectedTimeInterval(val timeInterval: TimeIntervalCode) : Action
        data class SelectedCafe(val cafeUuid: String?) : Action
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
