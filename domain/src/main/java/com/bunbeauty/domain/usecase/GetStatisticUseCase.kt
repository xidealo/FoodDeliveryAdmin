package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.StatisticRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetStatisticUseCase @Inject constructor(
    private val statisticRepo: StatisticRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        cafeUuid: String?,
        period: String
    ): List<Statistic> {
        return statisticRepo.getStatistic(
            token = dataStoreRepo.token.first(),
            cafeUuid = cafeUuid,
            period = period,
        )
    }
}