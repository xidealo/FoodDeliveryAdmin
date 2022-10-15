package com.bunbeauty.fooddeliveryadmin.screen.statistic

import com.bunbeauty.fooddeliveryadmin.shared.cafe.CafeUi

data class StatisticState(
    val cafeList: List<CafeUi> = emptyList(),
    val selectedTimeInterval: TimeInterval = TimeInterval.MONTH,
    val statisticList: List<StatisticItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val isCafesOpen: Boolean = false,
    val isTimeIntervalsOpen: Boolean = false
) {

    val selectedCafe: CafeUi?
        get() = cafeList.find { cafe ->
            cafe.isSelected
        }
}

enum class TimeInterval {
    DAY,
    WEEK,
    MONTH,
}