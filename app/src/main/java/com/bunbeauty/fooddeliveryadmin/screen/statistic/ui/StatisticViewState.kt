package com.bunbeauty.fooddeliveryadmin.screen.statistic.ui

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.feature.statistic.TimeIntervalCode
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class StatisticViewState(
    val statisticList: ImmutableList<StatisticItemModel>,
    val selectedCafe: String,
    val period: String,
    val isLoading: Boolean,
    val hasError: Boolean,
    val timeIntervalListUI: TimeIntervalListUI,
    val cafeListUI: CafeListUI
) : BaseViewState {
    @Immutable
    data class StatisticItemModel(
        val startMillis: Long,
        val period: String,
        val count: String,
        val proceeds: String,
        val date: String
    )
}

@Immutable
data class TimeIntervalListUI(
    val isShown: Boolean,
    val timeIntervalList: ImmutableList<TimeIntervalItem>
) {
    @Immutable
    data class TimeIntervalItem(
        val timeInterval: String,
        val timeIntervalType: TimeIntervalCode
    )
}

@Immutable
data class CafeListUI(
    val isShown: Boolean,
    val cafeList: ImmutableList<CafeItem>
) {
    @Immutable
    data class CafeItem(
        val uuid: String?,
        val name: String
    )
}
