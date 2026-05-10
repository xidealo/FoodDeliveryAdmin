package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.statistic.StatisticMapper
import com.bunbeauty.data.mapper.statistic.toDomain
import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.StatisticRepo
import kotlinx.coroutines.flow.firstOrNull

class StatisticRepository(
    private val networkConnector: FoodDeliveryApi,
    private val statisticMapper: StatisticMapper,
    private val dataStoreRepo: DataStoreRepo,
) : StatisticRepo {
    override suspend fun getStatistic(
        token: String,
        cafeUuid: String?,
        period: String,
    ): List<Statistic> =
        networkConnector
            .getStatistic(token, cafeUuid, period)
            .map(statisticMapper::toModel)

    override suspend fun getStatisticDayDetail(date: String): StatisticDayDetail {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return networkConnector
            .getStatisticDayDetail(
                token = token,
                companyUuid = companyUuid,
                date = date,
            ).toDomain()
    }
}
