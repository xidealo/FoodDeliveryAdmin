package com.bunbeauty.domain.use_case

import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.StatisticRepo
import javax.inject.Inject

class GetStatisticUseCase @Inject constructor(
    private val statisticRepo: StatisticRepo
) {
    suspend operator fun invoke(
        token: String,
        cafeUuid: String?,
        period: String
    ): List<Statistic> {
        return statisticRepo.getStatistic(
            token = token,
            cafeUuid = cafeUuid,
            period = period,
        )
    }
}