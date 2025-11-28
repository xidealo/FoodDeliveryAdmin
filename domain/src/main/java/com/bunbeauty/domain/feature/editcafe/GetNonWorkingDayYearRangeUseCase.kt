package com.bunbeauty.domain.feature.editcafe

import java.time.LocalDate

class GetNonWorkingDayYearRangeUseCase {

    operator fun invoke(): IntRange {
        val now = LocalDate.now()
        val afterMonth = now.plusMonths(1)
        return IntRange(now.year, afterMonth.year)
    }
}
