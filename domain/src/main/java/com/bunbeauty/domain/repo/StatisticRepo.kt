package com.bunbeauty.domain.repo

import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.model.statistic.Statistic

interface StatisticRepo {
    suspend fun getStatistic(
        token: String,
        cafeUuid: String,
        period: String
    ): ApiResult<List<Statistic>>
}