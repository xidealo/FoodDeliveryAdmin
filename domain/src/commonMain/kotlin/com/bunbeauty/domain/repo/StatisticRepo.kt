package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.model.statistic.StatisticDayDetail

interface StatisticRepo {
    suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String,
    ): List<Statistic>

    suspend fun getStatisticDayDetail(
        token: String,
        companyUuid: String,
        date: String,
    ): StatisticDayDetail
}
