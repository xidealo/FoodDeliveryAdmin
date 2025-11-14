package com.bunbeauty.fooddeliveryadmin.screen.statistic.ui

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.feature.statistic.TimeIntervalCode
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class StatisticViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val statisticList: ImmutableList<StatisticItemModel>,
            val period: String,
            val timeIntervalListUI: TimeIntervalListUI,
            val cafeAddress: String,
            val loadingStatistic: Boolean,
        ) : State {
            @Immutable
            data class StatisticItemModel(
                val startMillis: Long,
                val period: String,
                val count: String,
                val proceeds: String,
                val date: String,
            )
        }
    }

    @Immutable
    data class TimeIntervalListUI(
        val isShown: Boolean,
        val timeIntervalList: ImmutableList<TimeIntervalItem>,
    ) {
        @Immutable
        data class TimeIntervalItem(
            val timeInterval: String,
            val timeIntervalType: TimeIntervalCode,
        )
    }
}
