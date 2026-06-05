package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.model.statistic.StatisticDetailPeriod

interface StatisticRepo {
    suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String,
    ): List<Statistic>

    suspend fun getStatisticDayDetail(
        date: String,
        period: StatisticDetailPeriod,
    ): StatisticDayDetail
}
