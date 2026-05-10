package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.statistic.StatisticDayDetailMapper
import com.bunbeauty.data.mapper.statistic.StatisticMapper
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.repo.StatisticRepo

class StatisticRepository(
    private val networkConnector: FoodDeliveryApi,
    private val statisticMapper: StatisticMapper,
    private val statisticDayDetailMapper: StatisticDayDetailMapper,
) : StatisticRepo {
    override suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String,
    ): List<Statistic> =
        networkConnector
            .getStatistic(token, cafeUuid, period)
            .map(statisticMapper::toModel)

    override suspend fun getStatisticDayDetail(
        token: String,
        companyUuid: String,
        date: String,
    ): StatisticDayDetail =
        statisticDayDetailMapper.toModel(
            networkConnector.getStatisticDayDetail(
                token = token,
                companyUuid = companyUuid,
                date = date,
            ),
        )
}
