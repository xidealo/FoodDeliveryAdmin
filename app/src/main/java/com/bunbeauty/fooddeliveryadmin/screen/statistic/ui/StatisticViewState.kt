package com.bunbeauty.fooddeliveryadmin.screen.statistic.ui

import androidx.compose.runtime.Stable
import com.bunbeauty.presentation.feature.statistic.Statistic
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Stable
data class StatisticViewState(
    val statisticList: ImmutableList<StatisticItemModel>,
    val selectedCafe: String,
    val period: String
) : BaseViewState{
    data class StatisticItemModel(
        val startMillis: Long,
        val period: String,
        val count: String,
        val proceeds: String,
        val date: String
    )
}
