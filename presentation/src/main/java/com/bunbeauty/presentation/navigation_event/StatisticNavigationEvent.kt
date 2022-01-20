package com.bunbeauty.presentation.navigation_event

import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.presentation.model.ListData

sealed class StatisticNavigationEvent: NavigationEvent() {
    data class ToPeriodList(val listData: ListData): StatisticNavigationEvent()
    data class ToCafeAddressList(val listData: ListData): StatisticNavigationEvent()
    data class ToStatisticDetails(val statistic: Statistic): StatisticNavigationEvent()
}
