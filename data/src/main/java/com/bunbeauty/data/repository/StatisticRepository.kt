package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.statistic.StatisticMapper
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.StatisticRepo
import javax.inject.Inject

class StatisticRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val statisticMapper: StatisticMapper
) : StatisticRepo {
    override suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String
    ): List<Statistic> {
        return networkConnector
            .getStatistic(token, cafeUuid, period)
            .map(statisticMapper::toModel)
    }
}
