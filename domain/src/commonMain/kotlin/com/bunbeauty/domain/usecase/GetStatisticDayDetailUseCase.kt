package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.model.statistic.StatisticDetailPeriod
import com.bunbeauty.domain.repo.StatisticRepo

class GetStatisticDayDetailUseCase(
    private val statisticRepo: StatisticRepo,
) {
    suspend operator fun invoke(
        date: String,
        period: StatisticDetailPeriod,
    ): StatisticDayDetail = statisticRepo.getStatisticDayDetail(date, period)
}
