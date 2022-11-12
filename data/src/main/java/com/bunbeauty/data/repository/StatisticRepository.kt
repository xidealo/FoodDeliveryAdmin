package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.mapper.statistic.IStatisticMapper
import javax.inject.Inject

class StatisticRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val statisticMapper: IStatisticMapper
) {
    suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String
    ): ApiResult<List<Statistic>> {
        return when (val result = networkConnector.getStatistic(token, cafeUuid, period)) {
            is ApiResult.Success -> {
                ApiResult.Success(result.data.results.map(statisticMapper::toModel))
            }
            is ApiResult.Error -> {
                ApiResult.Error(result.apiError)
            }
        }
    }
}