package com.bunbeauty.presentation.navigator

import com.bunbeauty.data.model.Statistic

interface StatisticNavigator {
    fun getStatistic()
    fun goToSelectedStatistic(statistic: Statistic)
}