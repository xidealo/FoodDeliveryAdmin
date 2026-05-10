package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.StatisticRepo
import kotlinx.coroutines.flow.firstOrNull

class GetStatisticDayDetailUseCase(
    private val statisticRepo: StatisticRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(date: String): StatisticDayDetail {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return statisticRepo.getStatisticDayDetail(
            token = token,
            companyUuid = companyUuid,
            date = date,
        )
    }
}
