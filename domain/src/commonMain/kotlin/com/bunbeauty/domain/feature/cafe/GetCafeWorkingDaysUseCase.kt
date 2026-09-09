package com.bunbeauty.domain.feature.cafe

import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.model.cafe.CafeWorkingDay

class GetCafeWorkingDaysUseCase(
    private val getCafeUseCase: GetCafeUseCase,
) {
    suspend operator fun invoke(): List<CafeWorkingDay> {
        val cafe = getCafeUseCase()
        val daysByWeek = cafe.workingDays.associateBy { workingDay -> workingDay.dayOfWeek }

        return (MONDAY..SUNDAY).map { dayOfWeek ->
            daysByWeek[dayOfWeek] ?: CafeWorkingDay(
                dayOfWeek = dayOfWeek,
                fromTime = cafe.fromTime,
                toTime = cafe.toTime,
            )
        }
    }

    private companion object {
        const val MONDAY = 1
        const val SUNDAY = 7
    }
}
