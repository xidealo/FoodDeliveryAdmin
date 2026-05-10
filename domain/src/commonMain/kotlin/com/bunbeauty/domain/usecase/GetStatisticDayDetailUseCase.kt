package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.domain.repo.StatisticRepo

class GetStatisticDayDetailUseCase(
    private val statisticRepo: StatisticRepo,
) {
    suspend operator fun invoke(date: String): StatisticDayDetail = statisticRepo.getStatisticDayDetail(date)
}
