package com.bunbeauty.fooddeliveryadmin.screen.statistic

import com.bunbeauty.presentation.model.StatisticItemModel

data class StatisticState(
    val cafeList: List<Cafe> = emptyList(),
    val timeInterval: TimeInterval = TimeInterval.MONTH,
    val statisticList: List<StatisticItemModel> = emptyList(),
    val isLoading: Boolean = false
) {

    val selectedCafe: Cafe?
        get() = cafeList.find { cafe ->
            cafe.isSelected
        }
}

data class Cafe(
    val uuid: String?,
    val title: String,
    val isSelected: Boolean
)

enum class TimeInterval {
    DAY,
    WEEK,
    MONTH,
}