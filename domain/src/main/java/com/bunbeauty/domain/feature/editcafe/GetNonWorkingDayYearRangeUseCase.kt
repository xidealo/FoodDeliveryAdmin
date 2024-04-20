package com.bunbeauty.domain.feature.editcafe

import java.time.LocalDate
import javax.inject.Inject

class GetNonWorkingDayYearRangeUseCase @Inject constructor() {

    operator fun invoke(): IntRange {
        val now = LocalDate.now()
        val afterMonth = now.plusMonths(1)
        return IntRange(now.year, afterMonth.year)
    }
}
