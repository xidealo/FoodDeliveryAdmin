package com.bunbeauty.fooddeliveryadmin.ui.statistic

import com.bunbeauty.fooddeliveryadmin.data.model.Statistic

interface StatisticNavigator {
    fun getStatistic()
    fun goToSelectedStatistic(statistic: Statistic)
}