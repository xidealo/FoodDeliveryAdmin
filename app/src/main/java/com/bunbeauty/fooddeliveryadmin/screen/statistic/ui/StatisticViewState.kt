package com.bunbeauty.fooddeliveryadmin.screen.statistic.ui

import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class  StatisticViewState(
    val statisticList: ImmutableList<StatisticItemModel>,
    val selectedCafe: String,
    val period: String,
    val isLoading: Boolean,
    val hasError: Boolean
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
