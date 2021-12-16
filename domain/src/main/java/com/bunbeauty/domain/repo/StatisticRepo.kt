package com.bunbeauty.domain.repo

import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.model.statistic.Statistic
import kotlinx.coroutines.flow.Flow

interface StatisticRepo {
    suspend fun getStatistic(period: String): ApiResult<List<Statistic>>
}