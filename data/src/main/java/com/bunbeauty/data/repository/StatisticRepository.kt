package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.mapper.statistic.IStatisticMapper
import com.bunbeauty.domain.repo.StatisticRepo
import javax.inject.Inject

class StatisticRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val statisticMapper: IStatisticMapper
) : StatisticRepo {

    override suspend fun getStatistic(
        token: String,
        cafeUuid: String,
        period: String
    ): List<Statistic> {
        return when (val result = networkConnector.getStatistic(token, cafeUuid, period)) {
            is ApiResult.Success -> {
                result.data.results.map(statisticMapper::toModel)
            }
            is ApiResult.Error -> {
                //ApiResult.Error(result.apiError)
                emptyList()
            }
        }
    }


}