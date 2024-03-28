package com.bunbeauty.fooddeliveryadmin.screen.statistic.ui

import androidx.compose.runtime.Stable
import com.bunbeauty.presentation.feature.statistic.Statistic
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Stable
data class StatisticViewState(
    val statisticList: ImmutableList<Statistic.ViewDataState.StatisticItemModel>,
    val selectedCafe: String,
    val period: String
) : BaseViewState