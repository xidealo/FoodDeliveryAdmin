package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.StatisticRepo
import javax.inject.Inject

class GetStatisticUseCase @Inject constructor(
    private val statisticRepo: StatisticRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        cafeUuid: String?,
        period: String
    ): List<Statistic> {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        return statisticRepo.getStatistic(
            token = token,
            cafeUuid = cafeUuid,
            period = period
        )
    }
}
