package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.statistic.Statistic

interface StatisticRepo {
    suspend fun getStatistic(
        token: String,
        cafeUuid: String,
        period: String
    ): List<Statistic>
}